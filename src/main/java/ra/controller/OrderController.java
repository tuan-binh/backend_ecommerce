package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	
	@GetMapping("/get_all")
	public ResponseEntity<List<OrderResponse>> getOrders(Authentication authentication) throws UserException {
		return new ResponseEntity<>(orderService.getOrders(authentication), HttpStatus.OK);
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
	
}
