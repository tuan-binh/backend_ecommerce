package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ra.exception.*;
import ra.model.dto.request.CheckoutRequest;
import ra.model.dto.response.CartItemResponse;
import ra.model.dto.response.OrderResponse;
import ra.service.orders.IOrderService;

import javax.mail.MessagingException;
import java.util.List;

@RestController
@RequestMapping("/order")
@CrossOrigin("*")
public class OrderController {
	
	@Autowired
	private IOrderService orderService;
	
	@GetMapping("/get_order")
	public ResponseEntity<List<OrderResponse>> getOrders(Authentication authentication) throws UserException {
		return new ResponseEntity<>(orderService.getOrders(authentication), HttpStatus.OK);
	}
	
	@GetMapping("/get_order/{orderId}")
	public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId, Authentication authentication) throws OrderException {
		return new ResponseEntity<>(orderService.getOrderById(orderId, authentication), HttpStatus.OK);
	}
	
	@GetMapping("/get_cart")
	public ResponseEntity<List<CartItemResponse>> getCarts(Authentication authentication) throws UserException, OrderException, MessagingException {
		return new ResponseEntity<>(orderService.getCarts(authentication), HttpStatus.OK);
	}
	
	@PostMapping("/buy/{productDetailId}")
	public ResponseEntity<CartItemResponse> addProductToOrders(@PathVariable Long productDetailId, Authentication authentication) throws UserException, ProductException, ProductDetailException, CartItemException {
		return new ResponseEntity<>(orderService.addProductToOrder(productDetailId, authentication), HttpStatus.CREATED);
	}
	
	@PutMapping("/plus/{orderDetailId}")
	public ResponseEntity<CartItemResponse> plusOrderDetail(@PathVariable Long orderDetailId, Authentication authentication) throws CartItemException, OrderException, UserException {
		return new ResponseEntity<>(orderService.plusOrderDetail(orderDetailId, authentication), HttpStatus.OK);
	}
	
	@PutMapping("/minus/{orderDetailId}")
	public ResponseEntity<CartItemResponse> minusOrderDetail(@PathVariable Long orderDetailId, Authentication authentication) throws CartItemException, OrderException {
		return new ResponseEntity<>(orderService.minusOrderDetail(orderDetailId, authentication), HttpStatus.OK);
	}
	
	@DeleteMapping("/remove/{orderDetailId}")
	public ResponseEntity<CartItemResponse> removeOrderDetail(@PathVariable Long orderDetailId, Authentication authentication) throws CartItemException, UserException, OrderException {
		return new ResponseEntity<>(orderService.removeOrderDetail(orderDetailId, authentication), HttpStatus.OK);
	}
	
	@DeleteMapping("/remove_all")
	public ResponseEntity<List<CartItemResponse>> removeAllYourCart(Authentication authentication) throws UserException, OrderException {
		return new ResponseEntity<>(orderService.removeAllInYourCart(authentication), HttpStatus.OK);
	}
	
	@PostMapping("/check_out")
	public ResponseEntity<OrderResponse> checkoutYourCart(@RequestBody CheckoutRequest checkoutRequest, Authentication authentication) throws UserException, OrderException, CouponException, MessagingException {
		return new ResponseEntity<>(orderService.checkoutYourCart(checkoutRequest, authentication), HttpStatus.OK);
	}
	
	@PutMapping("/cancel_order/{orderId}")
	public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long orderId, Authentication authentication) throws UserException, OrderException, MessagingException {
		return new ResponseEntity<>(orderService.cancelOrder(orderId, authentication), HttpStatus.OK);
	}
	
	@PutMapping("/change_delivery/{orderId}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<OrderResponse> changeDeliveryOrder(@RequestParam("typeDelivery") String typeDelivery, @PathVariable Long orderId) throws OrderException {
		return new ResponseEntity<>(orderService.changeDelivery(typeDelivery, orderId), HttpStatus.OK);
	}
	
}
