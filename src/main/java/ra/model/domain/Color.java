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
@Table(name = "colors")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Color {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "color_name", nullable = false)
	private String colorName;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "color")
	@JsonIgnore
	Set<ProductDetail> productDetailSet = new HashSet<>();
	
	private boolean status;
	
}
