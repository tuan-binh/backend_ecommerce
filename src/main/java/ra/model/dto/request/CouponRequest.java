package ra.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CouponRequest {
	
	@NotNull(message = "coupon not null")
	@NotBlank(message = "coupon not blank")
	@NotEmpty(message = "coupon not empty")
	private String coupon;
	
	@NotNull(message = "percent not null")
	@NotBlank(message = "percent not blank")
	@NotEmpty(message = "percent not empty")
	@Min(value = 0, message = "must be than 0.0")
	@Max(value = 1, message = "must be lower 1")
	private double percent;
	
	@NotNull(message = "stock not null")
	@NotBlank(message = "stock not blank")
	@NotEmpty(message = "stock not empty")
	@Min(value = 1, message = "must be than 0")
	private int stock;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date startDate;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date endDate;
	
	private boolean status = true;
	
}
