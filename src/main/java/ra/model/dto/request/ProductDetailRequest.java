package ra.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductDetailRequest {
	
	@NotNull(message = "productID not null")
	@NotBlank(message = "productID not blank")
	@NotEmpty(message = "productID not empty")
	private Long productId;
	
	@NotNull(message = "colorId not null")
	@NotBlank(message = "colorId not blank")
	@NotEmpty(message = "colorId not empty")
	private Long colorId;
	
	@NotNull(message = "sizeId not null")
	@NotBlank(message = "sizeId not blank")
	@NotEmpty(message = "sizeId not empty")
	private Long sizeId;
	
	@Max(value = 1, message = "must be than 0")
	private int stock;
	
	private boolean status = true;
}
