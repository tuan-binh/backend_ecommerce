package ra.mapper.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ra.exception.CouponException;
import ra.mapper.IGenericMapper;
import ra.model.domain.Coupon;
import ra.model.domain.EDelivered;
import ra.model.domain.Orders;
import ra.model.dto.request.OrderRequest;
import ra.model.dto.response.OrderResponse;
import ra.repository.ICouponRepository;
import ra.service.coupon.CouponService;

import java.util.Optional;

@Component
public class OrderMapper implements IGenericMapper<Orders, OrderRequest, OrderResponse> {
	
	@Autowired
	private ICouponRepository couponRepository;
	
	@Override
	public Orders toEntity(OrderRequest orderRequest) throws CouponException {
		return Orders.builder()
				  .eDelivered(findEDeliveredByString(orderRequest.getEDelivered()))
				  .deliveryTime(orderRequest.getDeliveryTime())
				  .location(orderRequest.getLocation())
				  .phone(orderRequest.getPhone())
				  .total(orderRequest.getTotal())
				  .coupon(findCouponById(orderRequest.getCouponId()))
				  .status(orderRequest.isStatus())
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
				  .coupon(orders.getCoupon())
				  .status(orders.isStatus())
				  .build();
	}
	
	public EDelivered findEDeliveredByString(String delivery) {
		switch (delivery) {
			case "pending":
				return EDelivered.PENDING;
			case "prepare":
				return EDelivered.PREPARE;
			case "delivery":
				return EDelivered.DELIVERY;
			case "success":
				return EDelivered.SUCCESS;
			case "cancel":
				return EDelivered.CANCEL;
		}
		throw new RuntimeException("Lỗi không đúng định dạng order");
	}
	
	public Coupon findCouponById(Long couponId) throws CouponException {
		Optional<Coupon> optionalCoupon = couponRepository.findById(couponId);
		return optionalCoupon.orElseThrow(() -> new CouponException("coupon not found"));
	}
	
}
