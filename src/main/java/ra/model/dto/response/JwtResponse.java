package ra.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.model.domain.Orders;
import ra.model.domain.Product;
import ra.model.domain.Rates;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class JwtResponse {
	private String token;
	private final String type = "Bearer";
	private String fullName;
	private String email;
	private String phone;
	private String address;
	private List<String> roles = new ArrayList<>();
	private List<Product> favourites = new ArrayList<>();
	private List<Rates> rates = new ArrayList<>();
	private List<Orders> orders = new ArrayList<>();
	private boolean status;
	
}
