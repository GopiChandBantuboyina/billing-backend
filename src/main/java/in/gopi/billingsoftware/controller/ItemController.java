package in.gopi.billingsoftware.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.gopi.billingsoftware.io.ItemRequest;
import in.gopi.billingsoftware.io.ItemResponse;
import in.gopi.billingsoftware.service.ItemService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ItemController {
	
	private final ItemService itemService;
	
	@PostMapping("/admin/items")
	@ResponseStatus(HttpStatus.CREATED)
	public ItemResponse addItem(@RequestPart("item") String itemString,
								@RequestPart("file") MultipartFile file)
	{
		ObjectMapper objectMapper = new ObjectMapper();
		ItemRequest itemRequest = null;
		
		try {
			itemRequest = objectMapper.readValue(itemString, ItemRequest.class);
			return itemService.add(itemRequest, file);
		}
		catch(JsonProcessingException e)
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error occured while parsing json");
		}
	}
	
	
	@GetMapping("/items")
	public List<ItemResponse> read()
	{
		return itemService.fetchItems();
	}
	
	
	@DeleteMapping("/admin/items/{itemId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteItem(@PathVariable String itemId)
	{
		try
		{
			itemService.deleteItem(itemId);
		}
		catch(Exception e)
		{
			new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found: "+itemId);
		}
	}
	
}
