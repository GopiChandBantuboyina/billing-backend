package in.gopi.billingsoftware.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {
	
	//which we want to take input from postman
	
	private String name;
	private String description;
	private String bgColor;
	

}
