package ra.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.model.domain.Product;
import ra.model.domain.Users;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RateResponse {
	private Long id;
	private int rating;
	private String content;
	private Users users;
	private Product product;
	private boolean status;
}
