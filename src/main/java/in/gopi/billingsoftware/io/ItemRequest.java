package in.gopi.billingsoftware.io;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
	
	private String name;
	private BigDecimal price;
	private String categoryId;
	private String description;

}
