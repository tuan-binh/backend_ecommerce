package ra.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "product_name")
	private String productName;
	
	@Column(name = "image_active")
	private String imageActive;
	
	private String description;
	
	private double price;
	
	private int countBuy;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id")
	private Category category;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
	@JsonIgnore
	private Set<ProductDetail> productDetails = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
	@JsonIgnore
	private List<ImageProduct> images = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
	@JsonIgnore
	private List<Rates> rates = new ArrayList<>();
	
	private boolean status;
	
}
