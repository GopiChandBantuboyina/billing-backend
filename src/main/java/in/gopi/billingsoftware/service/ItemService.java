package in.gopi.billingsoftware.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import in.gopi.billingsoftware.io.ItemRequest;
import in.gopi.billingsoftware.io.ItemResponse;

public interface ItemService {
	
	
		ItemResponse add(ItemRequest itemRequest, MultipartFile multipartFile);
		
		List<ItemResponse> fetchItems();
		
		void deleteItem(String itemId);
}
