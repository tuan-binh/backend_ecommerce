package ra.model.dto.request;

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
public class ProductDetailRequest {
	private Long productId;
	private Long colorId;
	private Long sizeId;
	private int stock;
	private boolean status;
}
