package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ra.exception.*;
import ra.model.dto.response.CartItemResponse;
import ra.model.dto.response.OrderResponse;
import ra.service.orders.IOrderService;

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
	
	@GetMapping("/get_cart")
	public ResponseEntity<List<CartItemResponse>> getCarts(Authentication authentication) throws UserException, OrderException {
		return new ResponseEntity<>(orderService.getCarts(authentication), HttpStatus.OK);
	}
	
	@GetMapping("/buy/{productDetailId}")
	public ResponseEntity<CartItemResponse> addProductToOrders(@PathVariable Long productDetailId, Authentication authentication) throws UserException, ProductException, ProductDetailException, CartItemException {
		return new ResponseEntity<>(orderService.addProductToOrder(productDetailId, authentication), HttpStatus.CREATED);
	}
	
	@GetMapping("/plus/{orderDetailId}")
	public ResponseEntity<CartItemResponse> plusOrderDetail(@PathVariable Long orderDetailId, Authentication authentication) throws CartItemException, OrderException, UserException {
		return new ResponseEntity<>(orderService.plusOrderDetail(orderDetailId, authentication), HttpStatus.OK);
	}
	
	@GetMapping("/minus/{orderDetailId}")
	public ResponseEntity<CartItemResponse> minusOrderDetail(@PathVariable Long orderDetailId, Authentication authentication) throws CartItemException, UserException, OrderException {
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
	
	@PutMapping("/add_coupon/{couponId}")
	public ResponseEntity<OrderResponse> addCouponToOrder(@PathVariable Long couponId, Authentication authentication) throws CouponException, UserException, OrderException {
		return new ResponseEntity<>(orderService.addCouponToOrder(couponId, authentication), HttpStatus.OK);
	}
	
	@PostMapping("/check_out")
	public ResponseEntity<OrderResponse> checkoutYourCart(Authentication authentication) throws UserException {
		return new ResponseEntity<>(orderService.checkoutYourCart(authentication), HttpStatus.OK);
	}
	
	@GetMapping("/cancel_order/{orderId}")
	public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long orderId, Authentication authentication) throws UserException, OrderException {
		return new ResponseEntity<>(orderService.cancelOrder(orderId, authentication), HttpStatus.OK);
	}
	
	@PutMapping("/change_delivery/{orderId}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<OrderResponse> changeDeliveryOrder(@RequestParam("typeDelivery") String typeDelivery, @PathVariable Long orderId) throws OrderException {
		return new ResponseEntity<>(orderService.changeDelivery(typeDelivery, orderId), HttpStatus.OK);
	}
	
}
