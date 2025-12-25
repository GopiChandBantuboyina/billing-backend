package in.gopi.billingsoftware.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.gopi.billingsoftware.entity.CategoryEntity;
import in.gopi.billingsoftware.io.CategoryRequest;
import in.gopi.billingsoftware.io.CategoryResponse;
import in.gopi.billingsoftware.repository.CategoryRepository;
import in.gopi.billingsoftware.repository.ItemRepository;
import in.gopi.billingsoftware.service.CategoryService;
import in.gopi.billingsoftware.service.FileUploadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final FileUploadService fileUploadService;
    private final ItemRepository itemRepository;  //added this after item module implemented
    
    // Add category with optional file
    public CategoryResponse add(CategoryRequest request, MultipartFile file) {
        CategoryEntity newCategory = convertToEntity(request);

        // Handle image upload
        if (file != null && !file.isEmpty()) {
            try {
                String imageUrl = fileUploadService.addFile(file, "categories");
                newCategory.setImageUrl(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Failed to store image", e);
            }
        }

        newCategory = categoryRepository.save(newCategory);
        return convertToResponse(newCategory);
    }

    @Override
    public List<CategoryResponse> read() {
        return categoryRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String categoryId) {
        CategoryEntity existingCategory = categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found: " + categoryId));

        // Delete the image file from local storage
        fileUploadService.deleteFile(existingCategory.getImageUrl());

        categoryRepository.delete(existingCategory);
    }

    private CategoryEntity convertToEntity(CategoryRequest request) {
        return CategoryEntity.builder()
                .categoryId(UUID.randomUUID().toString())
                .name(request.getName())
                .description(request.getDescription())
                .bgColor(request.getBgColor())
                .build();
    }

    private CategoryResponse convertToResponse(CategoryEntity newEntity) {
    	
    	Integer itemsCount = itemRepository.countByCategoryId(newEntity.getId());
    	
        return CategoryResponse.builder()
                .categoryId(newEntity.getCategoryId())
                .name(newEntity.getName())
                .description(newEntity.getDescription())
                .bgColor(newEntity.getBgColor())
                .imgUrl(newEntity.getImageUrl())
                .createdAt(newEntity.getCreatedAt())
                .updatedAt(newEntity.getUpdatedAt())
                .items(itemsCount)
                .build();
    }
}
