package ra.service.coupon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.exception.CouponException;
import ra.mapper.coupon.CouponMapper;
import ra.model.domain.Coupon;
import ra.model.dto.request.CouponRequest;
import ra.model.dto.response.CouponResponse;
import ra.repository.ICouponRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CouponService implements ICouponService {
	
	@Autowired
	private ICouponRepository couponRepository;
	@Autowired
	private CouponMapper couponMapper;
	
	@Override
	public Page<CouponResponse> findAll(Pageable pageable) {
		List<CouponResponse> list = couponRepository.findAll(pageable).stream()
				  .map(item -> couponMapper.toResponse(item))
				  .collect(Collectors.toList());
		return new PageImpl<>(list, pageable, list.size());
	}
	
	@Override
	public CouponResponse findById(Long id) throws CouponException {
		Optional<Coupon> optionalCoupon = couponRepository.findById(id);
		return optionalCoupon.map(item -> couponMapper.toResponse(item)).orElseThrow(() -> new CouponException("coupon not found"));
	}
	
	@Override
	public CouponResponse save(CouponRequest couponRequest) {
		return couponMapper.toResponse(couponRepository.save(couponMapper.toEntity(couponRequest)));
	}
	
	@Override
	public CouponResponse update(CouponRequest couponRequest, Long id) {
		Coupon coupon = couponMapper.toEntity(couponRequest);
		coupon.setId(id);
		return couponMapper.toResponse(couponRepository.save(coupon));
	}
	
	@Override
	public CouponResponse changeStatus(Long id) throws CouponException {
		Coupon coupon = findCouponById(id);
		coupon.setStatus(!coupon.isStatus());
		return couponMapper.toResponse(couponRepository.save(coupon));
	}
	
	public Coupon findCouponById(Long id) throws CouponException {
		Optional<Coupon> optionalCoupon = couponRepository.findById(id);
		return optionalCoupon.orElseThrow(() -> new CouponException("coupon not found"));
	}
	
}
