package ra.security.user_principle;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ra.model.domain.Orders;
import ra.model.domain.Product;
import ra.model.domain.Rates;
import ra.model.domain.Users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserPrinciple implements UserDetails {
	
	private Long id;
	
	private String fullName;
	
	private String email;
	
	@JsonIgnore
	private String password;
	
	private String phone;
	
	private String address;
	private List<Product> favourites = new ArrayList<>();
	private List<Rates> rates = new ArrayList<>();
	private List<Orders> orders = new ArrayList<>();
	
	private Collection<? extends GrantedAuthority> authorities;
	private boolean status;
	
	public static UserDetails build(Users users) {
		return UserPrinciple.builder()
				  .id(users.getId())
				  .fullName(users.getFullName())
				  .email(users.getEmail())
				  .password(users.getPassword())
				  .phone(users.getPhone())
				  .address(users.getAddress())
				  .favourites(users.getFavourites())
				  .rates(users.getRates())
				  .orders(users.getOrders())
				  .authorities(users.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getRoleName().name())).collect(Collectors.toList()))
				  .status(users.isStatus())
				  .build();
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}
	
	@Override
	public String getUsername() {
		return this.email;
	}
	
	@Override
	public String getPassword() {
		return this.password;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}
	
}
