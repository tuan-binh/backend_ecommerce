package ra.model.dto.request;

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
public class RateRequest {
	private int rating;
	private String content;
//	private Users users;
//	private Product product;
	private boolean status;
}
