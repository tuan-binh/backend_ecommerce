package ra.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RateRequest {
	@Min(value = 1, message = "must be than 0")
	@Max(value = 10, message = "must be lower or equal 10")
	private int rating;
	
	@NotNull(message = "content not null")
	@NotBlank(message = "content not blank")
	@NotEmpty(message = "content not empty")
	private String content;
	
	@NotNull(message = "productId not null")
	@NotBlank(message = "productId not blank")
	@NotEmpty(message = "productId not empty")
	private Long productId;
	
}
