package ra.mapper.orders;

import org.springframework.stereotype.Component;
import ra.mapper.IGenericMapper;
import ra.model.domain.EDelivered;
import ra.model.domain.Orders;
import ra.model.dto.request.OrderRequest;
import ra.model.dto.response.OrderResponse;

@Component
public class OrderMapper implements IGenericMapper<Orders, OrderRequest, OrderResponse> {
	
	@Override
	public Orders toEntity(OrderRequest orderRequest) {
		return Orders.builder()
				  .eDelivered(findEDeliveredByString(orderRequest.getEDelivered()))
				  .deliveryTime(orderRequest.getDeliveryTime())
				  .location(orderRequest.getLocation())
				  .phone(orderRequest.getPhone())
				  .total(orderRequest.getTotal())
				  .users(orderRequest.getUsers())
				  .build();
	}
	
	@Override
	public OrderResponse toResponse(Orders orders) {
		return OrderResponse.builder()
				  .id(orders.getId())
				  .eDelivered(orders.getEDelivered().toString())
				  .deliveryTime(orders.getDeliveryTime())
				  .location(orders.getLocation())
				  .phone(orders.getPhone())
				  .total(orders.getTotal())
				  .users(orders.getUsers())
				  .build();
	}
	
	public EDelivered findEDeliveredByString(String delivery) {
		switch (delivery) {
			case "pending":
				return EDelivered.ROLE_PENDING;
			case "prepare":
				return EDelivered.ROLE_PREPARE;
			case "delivery":
				return EDelivered.ROLE_DELIVERY;
			case "success":
				return EDelivered.ROLE_SUCCESS;
			case "cancel":
				return EDelivered.ROLE_CANCEL;
		}
		throw new RuntimeException("Lỗi không đúng định dạng order");
	}
	
}
