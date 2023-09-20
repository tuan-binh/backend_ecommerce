package ra.model.dto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CheckoutRequest {
	private Long couponId = null;
	private String address = null;
	private String phone = null;
}
