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
public class CategoryRequest {
	
	@NotNull(message = "categoryName not null")
	@NotBlank(message = "categoryName not blank")
	@NotEmpty(message = "categoryName not empty")
	private String categoryName;
	private boolean status = true;
}
