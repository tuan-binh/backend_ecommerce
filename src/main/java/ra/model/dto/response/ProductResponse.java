package ra.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.model.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductResponse {
	
	private Long id;
	
	private String productName;
	
	private String imageActive;
	
	private String description;
	
	private double price;
	
	private int stock;
	
	private int viewCount;
	
	private Category category;
	
	private Set<Color> color;
	
	private Set<Size> size;
	
	private List<String> images;
	
	private List<Rates> rates = new ArrayList<>();
}
