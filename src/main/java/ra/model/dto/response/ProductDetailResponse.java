package ra.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.model.domain.Color;
import ra.model.domain.Product;
import ra.model.domain.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductDetailResponse {
	private Long id;
	
	private String product;
	
	private String color;
	
	private String size;
	
	private int stock;
	
	private boolean status;
}
