package ra.model.dto.response;

import lombok.*;

@NoArgsConstructor
@Setter
@Getter
@Builder
public class CountOrderByUser {
	private String fullName;
	private String email;
	private Long total;
	
	public CountOrderByUser(String fullName, String email, Long total) {
		this.fullName = fullName;
		this.email = email;
		this.total = total;
	}
}
