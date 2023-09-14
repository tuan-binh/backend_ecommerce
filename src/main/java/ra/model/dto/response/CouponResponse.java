package ra.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CouponResponse {
	
	private Long id;
	
	private String coupon;
	
	private int stock;
	
	private Date startDate;
	
	private Date endDate;
	
	private boolean status;
}
