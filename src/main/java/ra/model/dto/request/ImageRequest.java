package ra.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ImageRequest {
	private List<MultipartFile> image;
	@NotNull(message = "productId not null")
	@NotBlank(message = "productId not blank")
	@NotEmpty(message = "productId not empty")
	private Long productId;
}
