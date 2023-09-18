package ra.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.model.domain.Category;
import ra.model.domain.Color;
import ra.model.domain.Size;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductUpdate {
	private String productName;
	
	private String description;
	
	private double price;
	
	private int stock;
	
	private int viewCount;
	
	private Long categoryId;
	
	private boolean status;
}
