package ra.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CartItemResponse {
	private Long id;
	private String productName;
	private String url;
	private double price;
	private String color;
	private String size;
	private int quantity;
}
