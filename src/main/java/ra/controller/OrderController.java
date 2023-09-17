package ra.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.model.dto.response.OrderResponse;

@RestController
@RequestMapping("/order")
@CrossOrigin("*")
public class OrderController {
	
	@PostMapping("/buy/{productId}/in_cart/{userId}")
	public ResponseEntity<OrderResponse> handleAddProductInCartUser(@PathVariable Long productId, @PathVariable Long userId) {
		// thực hiện action add to cart user

//		return new ResponseEntity<>(, HttpStatus.OK);
		return null;
	}
	
	@PostMapping("/add/{productId}/in_cart/{userId}")
	public ResponseEntity<OrderResponse> handleAddMoreProductInCartUser(@PathVariable Long productId, @PathVariable Long userId) {
		// Thực hiện action add more product in cart user
//		return new ResponseEntity<>("Add Success", HttpStatus.OK);
		return null;
	}
	
	@PostMapping("/minus/{productId}/in_cart/{userId}")
	public ResponseEntity<OrderResponse> handleMinusProductInCartUser(@PathVariable Long productId, @PathVariable Long userId) {
		// Thực hiện action minus product in cart user
//		return new ResponseEntity<>("Add Success", HttpStatus.OK);
		return null;
	}
	
	@GetMapping("/get_order/{id}")
	public ResponseEntity<OrderResponse> findOrderById(@PathVariable Long id) {
		
		return null;
	}
	
	@GetMapping("/cancel_order/{id}")
	public ResponseEntity<OrderResponse> handleCancelOrder(@PathVariable Long id) {
		
		return null;
	}
	
	@GetMapping("/change_delevery/{id}")
	public ResponseEntity<OrderResponse> handleChangeDeliveryOrder(@PathVariable Long id) {
		// Thực hiện action change delivery in order
		
		return null;
	}
	
	
	
}
