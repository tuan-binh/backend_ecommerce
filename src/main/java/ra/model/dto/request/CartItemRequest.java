package ra.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CartItemRequest {
	private Long productDetailId;
	private Long orderId;
	private double price;
	private int quantity;
	private boolean status;
}
