package ra.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.model.domain.Orders;
import ra.model.domain.Product;
import ra.model.domain.Rates;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserResponse {
	private Long id;
	private String fullName;
	private String email;
	private String phone;
	private String address;
	private Set<String> roles = new HashSet<>();
	private List<Product> favourites = new ArrayList<>();
	private List<Rates> rates = new ArrayList<>();
	private List<Orders> orders = new ArrayList<>();
	private boolean status;
}
