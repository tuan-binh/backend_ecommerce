package ra.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.model.domain.Orders;
import ra.model.domain.ProductDetail;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CartItemResponse {
	private Long id;
	private double price;
	private int quantity;
}
