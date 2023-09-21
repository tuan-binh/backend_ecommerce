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
	@Size(min = 10,max = 11 ,message = "must be than 10 or lower 11 number")
	private String phone;
	
	@NotNull(message = "address not null")
	@NotBlank(message = "address not blank")
	@NotEmpty(message = "address not empty")
	private String address;
}
