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
public class SizeRequest {
	
	@NotNull(message = "sizeName not null")
	@NotBlank(message = "sizeName not blank")
	@NotEmpty(message = "sizeName not empty")
	private String sizeName;
	
	private boolean status = true;
}
