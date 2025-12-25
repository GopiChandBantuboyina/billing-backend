package in.gopi.billingsoftware.io;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
	//which we want to return from the controller
	
	private String categoryId;
	private String name;
	private String description;
	private String bgColor;
	
	private String imgUrl; 
	private Integer items; //Added this after item module implementation.
	private Timestamp createdAt;
	private Timestamp updatedAt;
	
}
