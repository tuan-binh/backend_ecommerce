package ra.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
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
	
	private double percent;
	
	private int stock;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date startDate;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date endDate;
	
	private boolean status;
	
}
