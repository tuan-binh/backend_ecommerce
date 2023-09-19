package ra.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RateResponse {
	private Long id;
	private String user;
	private int rating;
	private String content;
	private boolean status;
}
