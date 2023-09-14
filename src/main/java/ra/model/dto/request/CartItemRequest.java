package ra.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.model.domain.Product;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CartItemRequest {
	private Product product;
	private double price;
	private int quantity;
	private boolean status;
}
