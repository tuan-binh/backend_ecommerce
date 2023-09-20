package ra.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductRequest {
	
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
	
	@Min(value = 0, message = "must be than 0")
	private int countBuy;
	
	@NotNull(message = "categoryId not null")
	@NotBlank(message = "categoryId not blank")
	@NotEmpty(message = "categoryId not empty")
	private Long categoryId;
	
	private List<MultipartFile> file = new ArrayList<>();
	
	private boolean status = true;
	
}