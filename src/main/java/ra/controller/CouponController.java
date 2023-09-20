package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ra.exception.CouponException;
import ra.model.dto.request.CouponRequest;
import ra.model.dto.response.CouponResponse;
import ra.service.coupon.ICouponService;

@RestController
@RequestMapping("/coupon")
@CrossOrigin("*")
public class CouponController {
	
	@Autowired
	private ICouponService couponService;
	
	@GetMapping("/get_all")
	public ResponseEntity<Page<CouponResponse>> getAllCoupon(@PageableDefault(page = 0, size = 3) Pageable pageable) {
		return new ResponseEntity<>(couponService.findAll(pageable), HttpStatus.OK);
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<CouponResponse> getCouponById(@PathVariable Long id) throws CouponException {
		return new ResponseEntity<>(couponService.findById(id), HttpStatus.OK);
	}
	
	@PostMapping("/add")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<CouponResponse> handleAddCoupon(@RequestBody CouponRequest couponRequest) throws CouponException {
		return new ResponseEntity<>(couponService.save(couponRequest), HttpStatus.CREATED);
	}
	
	@PutMapping("/update/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<CouponResponse> handleUpdateCoupon(@RequestBody CouponRequest couponRequest, @PathVariable Long id) {
		return new ResponseEntity<>(couponService.update(couponRequest, id), HttpStatus.OK);
	}
	
	@GetMapping("/change_status/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<CouponResponse> handleChangeStatus(@PathVariable Long id) throws CouponException {
		return new ResponseEntity<>(couponService.changeStatus(id),HttpStatus.OK);
	}
	
}
