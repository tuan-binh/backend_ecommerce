package ra.model.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class RevenueByMonth {
	private int month;
	private int quantity;
	private double revenue;
}
