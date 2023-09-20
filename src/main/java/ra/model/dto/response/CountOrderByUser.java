package ra.model.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class CountOrderByUser {
	private String fullName;
	private String email;
	private Integer quantity;
}
