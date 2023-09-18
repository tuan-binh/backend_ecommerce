package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ra.exception.ProductException;
import ra.exception.RateException;
import ra.exception.UserException;
import ra.model.dto.request.RateRequest;
import ra.model.dto.response.RateResponse;
import ra.service.rate.IRateService;

@RestController
@RequestMapping("rate")
@CrossOrigin("*")
public class RateController {
	
	@Autowired
	private IRateService rateService;
	
	@GetMapping("/get_all/{productId}")
	public ResponseEntity<Page<RateResponse>> getAllRateByProductId(@PageableDefault(page = 0, size = 3) Pageable pageable, @PathVariable Long productId) throws ProductException {
		return new ResponseEntity<>(rateService.findAllByProductId(pageable, productId), HttpStatus.OK);
	}
	
	@PostMapping("/add_rate/{productId}")
	public ResponseEntity<RateResponse> handleRateProductByUser(@RequestBody RateRequest rateRequest, @PathVariable Long productId, Authentication authentication) throws UserException, ProductException {
		return new ResponseEntity<>(rateService.rateProductByUser(rateRequest, productId, authentication), HttpStatus.CREATED);
	}
	
	@PutMapping("/update_rate/{rateId}/in/{productId}")
	public ResponseEntity<RateResponse> handleUpdateRateInProduct(@RequestBody RateRequest rateRequest, @PathVariable Long rateId, @PathVariable Long productId, Authentication authentication) throws RateException, UserException, ProductException {
		return new ResponseEntity<>(rateService.updateRateInProduct(rateRequest, rateId, productId, authentication), HttpStatus.OK);
	}
	
	@DeleteMapping("/remove_rate/{rateId}/in/{productId}")
	public ResponseEntity<RateResponse> handleRemoveRateByUser(@PathVariable Long rateId, @PathVariable Long productId, Authentication authentication) throws RateException, UserException, ProductException {
		return new ResponseEntity<>(rateService.removeRateInProductByUser(rateId, productId, authentication), HttpStatus.OK);
	}
	
}
