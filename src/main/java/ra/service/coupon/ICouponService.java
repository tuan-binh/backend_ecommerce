package ra.service.coupon;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.exception.CouponException;
import ra.model.dto.request.CouponRequest;
import ra.model.dto.response.CouponResponse;

public interface ICouponService {
	
	Page<CouponResponse> findAll(Pageable pageable);
	
	CouponResponse findById(Long id) throws CouponException;
	
	CouponResponse save(CouponRequest couponRequest) throws CouponException;
	
	CouponResponse update(CouponRequest couponRequest, Long id);
	
	CouponResponse changeStatus(Long id) throws CouponException;
	
}
