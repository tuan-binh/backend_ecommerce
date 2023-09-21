package ra.model.dto.response;

import lombok.*;

@NoArgsConstructor
@Setter
@Getter
@Builder
public class CountOrderByUser {
	private String fullName;
	private Long total;
	
	public CountOrderByUser(String fullName, Long total) {
		this.fullName = fullName;
		this.total = total;
	}
}
