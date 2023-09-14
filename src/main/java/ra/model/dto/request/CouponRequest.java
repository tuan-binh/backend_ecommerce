package ra.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CouponRequest {
	
	private String coupon;
	
	private int stock;

	private Date startDate;

	private Date endDate;
	
	private boolean status;
	
}
