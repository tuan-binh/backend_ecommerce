package ra.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.model.domain.Coupon;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderResponse {
	private Long id;
	private String eDelivered;
	private Date deliveryTime;
	private String location;
	private String phone;
	private double total;
	private Coupon coupon;
	private List<CartItemResponse> carts;
	private boolean status;
}
