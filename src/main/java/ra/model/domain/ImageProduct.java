package ra.model.domain;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "image_product")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class ImageProduct {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String image;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;
	
}
