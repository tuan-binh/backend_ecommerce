package ra.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sizes")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
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
