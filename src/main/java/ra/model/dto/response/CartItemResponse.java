package ra.model.dto.response;

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
public class CartItemResponse {
	private Long id;
	private Product product;
	private Orders orders;
	private double price;
	private int quantity;
	private boolean status;
}
