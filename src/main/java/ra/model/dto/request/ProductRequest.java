package ra.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductRequest {
	
	private String productName;
	
	private String description;
	
	private double price;
	
	private int viewCount;
	
	private Long categoryId;
	
	private List<MultipartFile> file = new ArrayList<>();
	
	private boolean status;
	
}
