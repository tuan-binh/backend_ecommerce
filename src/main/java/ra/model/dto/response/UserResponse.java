package ra.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.model.domain.Roles;

import java.util.HashSet;
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
	private boolean status;
}
