package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ra.model.domain.Orders;
import ra.security.user_principle.UserDetailService;
import ra.service.orders.IOrderService;

import java.util.List;

@RestController
@RequestMapping("/order")
@CrossOrigin("*")
public class OrderController {
	
	@Autowired
	private IOrderService orderService;
	
	@GetMapping("/getOrders")
	public ResponseEntity<List<Orders>> getOrders() {
//		List<Orders> orders = null;
//		if (authentication != null && authentication.isAuthenticated()) {
//			String username = authentication.getName(); // Lấy tên người dùng
//			Users users = userDetailService.findByEmail(username);
//			// Sau đó, bạn có thể sử dụng username để lấy order của người dùng.
//			orders = new ArrayList<>(users.getOrders());
//		}
//		// Trả về danh sách các đơn hàng của người dùng.
//		return new ResponseEntity<>(orders, HttpStatus.OK);
		return null;
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
