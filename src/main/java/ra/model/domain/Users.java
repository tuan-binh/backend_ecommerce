package ra.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class Users {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "full_name", nullable = false)
	private String fullName;
	
	@Column(unique = true)
	private String email;
	
	@JsonIgnore
	private String password;
	
	@Column(unique = true)
	@Size(min = 10,max = 11,message = "Phone number must be within 10 to 11 digits")
	private String phone;
	
	private String address;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			  name = "role_detail",
			  joinColumns = @JoinColumn(name = "user_id"),
			  inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private Set<Roles> roles = new HashSet<>();
	
	
	@ManyToMany
	@JoinTable(
			  name = "favourites",
			  joinColumns = @JoinColumn(name = "user_id"),
			  inverseJoinColumns = @JoinColumn(name = "product_id")
	)
	private List<Product> favourites = new ArrayList<>();
	
	@OneToMany( mappedBy = "users")
	@JsonIgnore
	private List<Rates> rates = new ArrayList<>();
	
	@OneToMany( mappedBy = "users")
	@JsonIgnore
	private List<Orders> orders = new ArrayList<>();
	
	private boolean status;
}
