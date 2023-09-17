package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.exception.ProductException;
import ra.exception.UserException;
import ra.model.dto.response.OrderResponse;
import ra.service.orders.IOrderService;

@RestController
@RequestMapping("/order")
@CrossOrigin("*")
public class OrderController {
	
	@Autowired
	private IOrderService orderService;
	
	@PostMapping("/buy/{productId}/in_cart/{userId}")
	public ResponseEntity<OrderResponse> handleAddProductInCartUser(@PathVariable Long productId, @PathVariable Long userId) throws UserException, ProductException {
		return new ResponseEntity<>(orderService.BuyProductInCartUser(productId, userId), HttpStatus.OK);
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
