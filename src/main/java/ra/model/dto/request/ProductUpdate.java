package ra.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductUpdate {
	
	@NotNull(message = "productName not null")
	@NotBlank(message = "productName not blank")
	@NotEmpty(message = "productName not empty")
	private String productName;
	
	@NotNull(message = "description not null")
	@NotBlank(message = "description not blank")
	@NotEmpty(message = "description not empty")
	private String description;
	
	@Min(value = 0, message = "must be than 0")
	private double price;
	
	@NotNull(message = "categoryId not null")
	@NotBlank(message = "categoryId not blank")
	@NotEmpty(message = "categoryId not empty")
	private Long categoryId;
	
	private boolean status = true;
}
