package ra.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ra.exception.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> invalidRequest(MethodArgumentNotValidException ex) {
		Map<String, String> err = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(c -> {
			err.put(c.getField(), c.getDefaultMessage());
		});
		return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(UserException.class)
	public String handleExceptionUser(UserException e) {
		return "Exception user --> " + e.getMessage();
	}
	
	@ExceptionHandler(RoleException.class)
	public String handleExceptionRole(RoleException e) {
		return "Exception role --> " + e.getMessage();
	}
	
	@ExceptionHandler(SizeException.class)
	public String handleExceptionSize(SizeException e) {
		return "Exception size --> " + e.getMessage();
	}
	
	@ExceptionHandler(RateException.class)
	public String handleExceptionRate(RateException e) {
		return "Exception rate --> " + e.getMessage();
	}
	
	@ExceptionHandler(CategoryException.class)
	public String handleExceptionCategory(CategoryException e) {
		return "Exception category --> " + e.getMessage();
	}
	
	@ExceptionHandler(CouponException.class)
	public String handleExceptionCoupon(CouponException e) {
		return "Exception coupon --> " + e.getMessage();
	}
	
}
