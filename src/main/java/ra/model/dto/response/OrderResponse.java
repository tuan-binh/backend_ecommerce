package ra.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.model.domain.EDelivered;
import ra.model.domain.Users;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderResponse {
	private Long id;
	private String eDelivered;
	private Date deliveryTime;
	private String location;
	private String phone;
	private double total;
	private Users users;
	private boolean status;
}
