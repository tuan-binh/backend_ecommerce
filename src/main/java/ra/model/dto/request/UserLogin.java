package ra.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserLogin {
	
	@NotEmpty(message = "Email Not Empty")
	@NotBlank(message = "Email Not Blank")
	@NotNull(message = "Email Not Null")
	private String email;
	
	@NotEmpty(message = "Password Not Empty")
	@NotBlank(message = "Password Not Blank")
	@NotNull(message = "Password Not Null")
	private String password;
	
}
