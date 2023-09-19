package ra.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sizes")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Size {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "size_name", nullable = false)
	private String sizeName;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "size")
	@JsonIgnore
	Set<ProductDetail> productDetails = new HashSet<>();
	
	private boolean status;
	
}
