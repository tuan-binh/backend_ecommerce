package ra.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
	private boolean status;
	
}
