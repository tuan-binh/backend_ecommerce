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
import ra.mapper.product.ProductMapper;
import ra.model.dto.request.RateRequest;
import ra.model.dto.response.ProductResponse;
import ra.model.dto.response.UserResponse;
import ra.service.user.IUserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {
	
	@Autowired
	private IUserService userService;
	@Autowired
	private ProductMapper productMapper;
	
	@GetMapping("/get_all")
	public ResponseEntity<Page<UserResponse>> getAllUsers(@PageableDefault(page = 0, size = 3) Pageable pageable, @RequestParam(value = "search", defaultValue = "") Optional<String> search) {
		return new ResponseEntity<>(userService.findAll(pageable, search), HttpStatus.OK);
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) throws UserException {
		return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
	}
	
	@GetMapping("/change_status/{id}")
	public ResponseEntity<UserResponse> handleChangeStatus(@PathVariable Long id) throws UserException {
		return new ResponseEntity<>(userService.changeStatus(id), HttpStatus.OK);
	}
	
	@GetMapping("/getFavourite")
	public ResponseEntity<List<ProductResponse>> getFavourite(Authentication authentication) throws UserException {
		return new ResponseEntity<>(userService.getFavourite(authentication), HttpStatus.OK);
	}
	
	@PostMapping("/favourite/{productId}")
	public ResponseEntity<List<ProductResponse>> handleAddProductToFavourite(@PathVariable Long productId, Authentication authentication) throws UserException, ProductException {
		return new ResponseEntity<>(userService.addProductToFavourite(productId, authentication), HttpStatus.CREATED);
	}
	
	@DeleteMapping("/favourite/{productId}")
	public ResponseEntity<List<ProductResponse>> handleRemoveProductInFavourite(@PathVariable Long productId, Authentication authentication) throws UserException, ProductException {
		return new ResponseEntity<>(userService.removeProductInFavourite(productId, authentication), HttpStatus.OK);
	}
	
	@PostMapping("/add_rate/{productId}")
	public ResponseEntity<ProductResponse> handleRateProductByUser(@RequestBody RateRequest rateRequest, @PathVariable Long productId, Authentication authentication) throws UserException, ProductException {
		return new ResponseEntity<>(userService.rateProductByUser(rateRequest, productId, authentication), HttpStatus.CREATED);
	}
	
	@PutMapping("/update_rate/{rateId}/in/{productId}")
	public ResponseEntity<ProductResponse> handleUpdateRateInProduct(@RequestBody RateRequest rateRequest, @PathVariable Long rateId, @PathVariable Long productId, Authentication authentication) throws RateException, UserException, ProductException {
		return new ResponseEntity<>(userService.updateRateInProduct(rateRequest, rateId, productId, authentication), HttpStatus.OK);
	}
	
	@DeleteMapping("/rate/{rateId}/in/{productId}")
	public ResponseEntity<ProductResponse> handleRemoveRateByUser(@PathVariable Long rateId, @PathVariable Long productId, Authentication authentication) throws RateException, UserException, ProductException {
		return new ResponseEntity<>(userService.removeRateInProductByUser(rateId, productId, authentication), HttpStatus.OK);
	}
	
}
