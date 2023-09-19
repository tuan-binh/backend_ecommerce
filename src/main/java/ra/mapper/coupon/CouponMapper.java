package ra.mapper.coupon;

import org.springframework.stereotype.Component;
import ra.mapper.IGenericMapper;
import ra.model.domain.Coupon;
import ra.model.dto.request.CouponRequest;
import ra.model.dto.response.CouponResponse;

@Component
public class CouponMapper implements IGenericMapper<Coupon, CouponRequest, CouponResponse> {
	
	@Override
	public Coupon toEntity(CouponRequest couponRequest) {
		return Coupon.builder()
				  .coupon(couponRequest.getCoupon())
				  .stock(couponRequest.getStock())
				  .percent(couponRequest.getPercent())
				  .startDate(couponRequest.getStartDate())
				  .endDate(couponRequest.getEndDate())
				  .status(couponRequest.isStatus())
				  .build();
	}
	
	@Override
	public CouponResponse toResponse(Coupon coupon) {
		return CouponResponse.builder()
				  .id(coupon.getId())
				  .coupon(coupon.getCoupon())
				  .percent(coupon.getPercent())
				  .stock(coupon.getStock())
				  .startDate(coupon.getStartDate())
				  .endDate(coupon.getEndDate())
				  .status(coupon.isStatus())
				  .build();
	}
}
