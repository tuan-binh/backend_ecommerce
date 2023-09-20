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
public class UserUpdate {
	
	@NotNull(message = "phone not null")
	@NotBlank(message = "phone not blank")
	@NotEmpty(message = "phone not empty")
	@Min(value = 10, message = "must be than or equal 10")
	@Max(value = 11, message = "must be lower or equal 11")
	private String phone;
	
	@NotNull(message = "address not null")
	@NotBlank(message = "address not blank")
	@NotEmpty(message = "address not empty")
	private String address;
}
