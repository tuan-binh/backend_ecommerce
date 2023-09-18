package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ra.exception.ProductException;
import ra.exception.UserException;
import ra.model.dto.response.OrderResponse;
import ra.service.orders.IOrderService;

import java.util.List;

@RestController
@RequestMapping("/order")
@CrossOrigin("*")
public class OrderController {
	
	@Autowired
	private IOrderService orderService;
	
	@GetMapping("/getOrders")
	public ResponseEntity<List<OrderResponse>> getOrders(Authentication authentication) throws UserException {
		return new ResponseEntity<>(orderService.getOrders(authentication), HttpStatus.OK);
	}
	
	@PostMapping("/buy/{productId}")
	public ResponseEntity<OrderResponse> addProductToOrders(@PathVariable Long productId, Authentication authentication) throws UserException, ProductException {
		return new ResponseEntity<>(orderService.addProductToOrder(productId, authentication), HttpStatus.CREATED);
	}

//	@PostMapping("/buy/{productId}/in_cart/{userId}")
//	public ResponseEntity<OrderResponse> handleAddProductInCartUser(@PathVariable Long productId, @PathVariable Long userId) throws UserException, ProductException {
//		return new ResponseEntity<>(orderService.buyProductInCartUser(productId, userId), HttpStatus.OK);
//	}
//
//	@PostMapping("/add_product/{productId}/to/{orderId}")
//	public ResponseEntity<OrderResponse> handleAddProductToOrder(@PathVariable Long productId,@PathVariable Long orderId) {
//		return new ResponseEntity<>(orderService.addProductToOrderUser(productId,orderId),HttpStatus.OK);
//	}
//
//	@PostMapping("/add/{cartId}/in_cart/{userId}")
//	public ResponseEntity<OrderResponse> handleAddMoreProductInCartUser(@PathVariable Long cartId, @PathVariable Long userId) {
//		// Thực hiện action add more product in cart user
////		return new ResponseEntity<>("Add Success", HttpStatus.OK);
//		return null;
//	}
//
//	@PostMapping("/minus/{cartId}/in_cart/{userId}")
//	public ResponseEntity<OrderResponse> handleMinusProductInCartUser(@PathVariable Long cartId, @PathVariable Long userId) {
//		// Thực hiện action minus product in cart user
////		return new ResponseEntity<>("Add Success", HttpStatus.OK);
//		return null;
//	}
//
//	@GetMapping("/get_order/{id}")
//	public ResponseEntity<OrderResponse> findOrderById(@PathVariable Long id) {
//
//		return null;
//	}
//
//	@GetMapping("/cancel_order/{id}")
//	public ResponseEntity<OrderResponse> handleCancelOrder(@PathVariable Long id) {
//
//		return null;
//	}
//
//	@GetMapping("/change_delevery/{id}")
//	public ResponseEntity<OrderResponse> handleChangeDeliveryOrder(@PathVariable Long id) {
//		// Thực hiện action change delivery in order
//
//		return null;
//	}
	
	
}
