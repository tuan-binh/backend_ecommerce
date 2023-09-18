package ra.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.model.domain.Coupon;
import ra.model.domain.Users;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderRequest {
	private String eDelivered;
	private Date deliveryTime;
	private String location;
	private String phone;
	private double total;
	private Long couponId;
	private boolean status;
	
}
