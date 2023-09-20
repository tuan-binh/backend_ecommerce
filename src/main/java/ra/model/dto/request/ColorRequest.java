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
public class ColorRequest {
	
	@NotNull(message = "colorName not null")
	@NotBlank(message = "colorName not blank")
	@NotEmpty(message = "colorName not empty")
	private String colorName;
	private boolean status = true;
}
