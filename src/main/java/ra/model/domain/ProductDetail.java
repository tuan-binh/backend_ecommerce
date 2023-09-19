package ra.model.domain;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_detail")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "color_id")
	private Color color;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "size_id")
	private Size size;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "productDetail")
	@JsonIgnore
	private List<CartItem> cartItems = new ArrayList<>();
	
	private int stock;
	
	private boolean status;
	
}
