package ra.model.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "order_detail")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CartItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_detail_id")
	private ProductDetail productDetail;
	
	@ManyToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Orders orders;
	
	private double price;
	
	private int quantity;
	
	
}
