package in.gopi.billingsoftware.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import in.gopi.billingsoftware.entity.CategoryEntity;
import in.gopi.billingsoftware.entity.ItemEntity;
import in.gopi.billingsoftware.io.ItemRequest;
import in.gopi.billingsoftware.io.ItemResponse;
import in.gopi.billingsoftware.repository.CategoryRepository;
import in.gopi.billingsoftware.repository.ItemRepository;
import in.gopi.billingsoftware.service.FileUploadService;
import in.gopi.billingsoftware.service.ItemService;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
	
	private final ItemRepository itemRepository;
	private final CategoryRepository categoryRepository;
	private final FileUploadService fileUploadService;

	@Override
	public ItemResponse add(ItemRequest request, MultipartFile file){
		
		ItemEntity newItem =  convertToEntity(request);
		
		// Handle image upload
        if (file != null && !file.isEmpty()) {
            try {
                String imageUrl = fileUploadService.addFile(file, "items");
                newItem.setImgUrl(imageUrl);
                
            } catch (IOException e) {
                throw new RuntimeException("Failed to store image", e);
            }
        }
		
		CategoryEntity existingCategory =  categoryRepository.findByCategoryId(request.getCategoryId())
				.orElseThrow(() -> new RuntimeException("Category not found: "+request.getCategoryId()));
		
		newItem.setCategory(existingCategory);
		newItem = itemRepository.save(newItem);
		
		return convertToResponse(newItem);
		
	}

	private ItemResponse convertToResponse(ItemEntity item) {
		
		return ItemResponse.builder()
				.itemId(item.getItemId())
				.name(item.getName())
				.price(item.getPrice())
				.description(item.getDescription())
				.imgUrl(item.getImgUrl())
				.categoryId(item.getCategory().getCategoryId())
				.createdAt(item.getCreatedAt())
				.updatedAt(item.getUpdatedAt())
				.build();
				
				
	}

	private ItemEntity convertToEntity(ItemRequest itemRequest) {
		
		return ItemEntity.builder()
				.itemId(UUID.randomUUID().toString())
				.name(itemRequest.getName())
				.price(itemRequest.getPrice())
				.description(itemRequest.getDescription())
				.build();
	}

	@Override
	public List<ItemResponse> fetchItems() {
		
		return itemRepository.findAll()
		.stream()
		.map(item -> convertToResponse(item))
		.collect(Collectors.toList());
		
	}

	@Override
	public void deleteItem(String itemId) {
		
		ItemEntity existingItem = itemRepository.findByItemId(itemId)
				.orElseThrow(() -> new RuntimeException("Item not found: "+itemId));
		
		boolean isFileDeleted = fileUploadService.deleteFile(existingItem.getImgUrl());
		
		if(isFileDeleted)
		{
			itemRepository.delete(existingItem);
			
		}
		else
		{
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to delete the image"); 
		}
		
		
	}

}
