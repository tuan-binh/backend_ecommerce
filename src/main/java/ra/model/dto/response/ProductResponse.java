package ra.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.model.domain.Category;
import ra.model.domain.ProductDetail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductResponse {
	
	private Long id;
	
	private String productName;
	
	private String imageActive;
	
	private String description;
	
	private double price;
	
	private Category category;
	
	private boolean status;
}
