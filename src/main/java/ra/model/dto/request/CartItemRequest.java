package ra.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.model.domain.Orders;
import ra.model.domain.Product;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CartItemRequest {
	private Long productId;
	private Long orderId;
	private double price;
	private int quantity;
	private boolean status;
}
