package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.exception.ProductException;
import ra.exception.RateException;
import ra.exception.UserException;
import ra.model.dto.request.RateRequest;
import ra.model.dto.response.UserResponse;
import ra.service.user.IUserService;

import java.util.Optional;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {
	
	@Autowired
	private IUserService userService;
	
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
	
	@GetMapping("/favourite/{productId}/to/{userId}")
	public ResponseEntity<UserResponse> handleAddProductToFavourite(@PathVariable Long productId, @PathVariable Long userId) throws UserException, ProductException {
		return new ResponseEntity<>(userService.addProductToFavourite(productId, userId), HttpStatus.CREATED);
	}
	
	@PostMapping("/rate/{productId}/by/{userId}")
	public ResponseEntity<UserResponse> handleRateProductByUser(@RequestBody RateRequest rateRequest, @PathVariable Long productId, @PathVariable Long userId) throws UserException, ProductException {
		return new ResponseEntity<>(userService.rateProductByUser(rateRequest, productId, userId), HttpStatus.CREATED);
	}
	
	@DeleteMapping("/favourite/{productId}/in/{userId}")
	public ResponseEntity<UserResponse> handleRemoveProductInFavourite(@PathVariable Long productId, @PathVariable Long userId) throws UserException, ProductException {
		return new ResponseEntity<>(userService.removeFavouriteInUser(productId, userId), HttpStatus.OK);
	}
	
	@DeleteMapping("/rate/{rateId}/by/{userId}")
	public ResponseEntity<UserResponse> handleRemoveRateByUser(@PathVariable Long rateId, @PathVariable Long userId) throws RateException, UserException {
		return new ResponseEntity<>(userService.removeRateInProductByUser(userId, rateId), HttpStatus.OK);
	}
	
	
	
}
