package ra.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import ra.model.domain.Category;
import ra.model.domain.Color;
import ra.model.domain.Size;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductUpdate {
	private String productName;
	
	private String description;
	
	private double price;
	
	private int stock;
	
	private int viewCount;
	
	private Category category;
	
	private Set<Color> color;
	
	private Set<Size> size;
	
	private boolean status;
}
