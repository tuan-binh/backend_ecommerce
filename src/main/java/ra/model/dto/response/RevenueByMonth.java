package ra.model.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class RevenueByMonth {
	private int month;
	private long quantity;
	private double revenue;
}
