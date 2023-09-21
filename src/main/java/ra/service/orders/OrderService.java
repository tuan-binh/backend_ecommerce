package ra.service.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ra.exception.*;
import ra.mapper.cartitem.CartItemMapper;
import ra.mapper.orders.OrderMapper;
import ra.model.domain.*;
import ra.model.dto.request.CheckoutRequest;
import ra.model.dto.request.OrderRequest;
import ra.model.dto.response.CartItemResponse;
import ra.model.dto.response.OrderResponse;
import ra.repository.*;
import ra.security.user_principle.UserPrinciple;
import ra.service.mail.MailService;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {
	
	@Autowired
	private IOrderRepository orderRepository;
	@Autowired
	private IProductRepository productRepository;
	@Autowired
	private ICartItemRepository cartItemRepository;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private CartItemMapper cartItemMapper;
	@Autowired
	private IUserRepository userRepository;
	@Autowired
	private ICouponRepository couponRepository;
	@Autowired
	private IProductDetailRepository productDetailRepository;
	@Autowired
	private MailService mailService;
	
	
	@Override
	public Page<OrderResponse> findAll(Pageable pageable, Optional<String> phone) {
		List<OrderResponse> list = phone.map(s -> orderRepository.findAllByPhone(pageable, s).stream()
				  .map(item -> orderMapper.toResponse(item))
				  .collect(Collectors.toList())).orElseGet(() -> orderRepository.findAll(pageable).stream()
				  .map(item -> orderMapper.toResponse(item))
				  .collect(Collectors.toList()));
		return new PageImpl<>(list, pageable, list.size());
	}
	
	@Override
	public OrderResponse findById(Long id) throws OrderException {
		Optional<Orders> optionalOrders = orderRepository.findById(id);
		return optionalOrders.map(item -> orderMapper.toResponse(item)).orElseThrow(() -> new OrderException("order not found"));
	}
	
	@Override
	public OrderResponse save(OrderRequest orderRequest) throws CouponException {
		return orderMapper.toResponse(orderRepository.save(orderMapper.toEntity(orderRequest)));
	}
	
	@Override
	public OrderResponse update(OrderRequest orderRequest, Long id) throws CouponException {
		Orders orders = orderMapper.toEntity(orderRequest);
		orders.setId(id);
		return orderMapper.toResponse(orderRepository.save(orders));
	}
	
	@Override
	public OrderResponse changeStatus(Long id) throws OrderException {
		Orders orders = findOrderById(id);
		orders.setStatus(!orders.isStatus());
		return orderMapper.toResponse(orderRepository.save(orders));
	}
	
	@Override
	public OrderResponse changeDelivery(String typeDelivery, Long id) throws OrderException, MessagingException {
		EDelivered type = findDeliveryByInput(typeDelivery);
		Orders orders = findOrderById(id);
		Users users = orders.getUsers();
		if (orders.getEDelivered().toString().equals("PENDING")) {
			if (type.toString().equals("DELIVERY") || type.toString().equals("SUCCESS")) {
				throw new OrderException("Your order is in status PENDING cannot switch to state " + type);
			}
			orders.setEDelivered(type);
		} else if (orders.getEDelivered().toString().equals("PREPARE")) {
			if (type.toString().equals("PENDING") || type.toString().equals("SUCCESS")) {
				throw new OrderException("Your order is in status PREPARE cannot switch to state " + type);
			}
			orders.setEDelivered(type);
		} else if (orders.getEDelivered().toString().equals("DELIVERY")) {
			if (type.toString().equals("PENDING") || type.toString().equals("PREPARE") || type.toString().equals("CANCEL")) {
				throw new OrderException("Your order is in status DELIVERY cannot switch to state " + type);
			}
			orders.setEDelivered(type);
		} else if (orders.getEDelivered().toString().equals("SUCCESS")) {
			if (type.toString().equals("PENDING") || type.toString().equals("PREPARE") || type.toString().equals("DELIVERY") || type.toString().equals("CANCEL")) {
				throw new OrderException("Your order is in status SUCCESS cannot switch to state " + type);
			}
			orders.setEDelivered(type);
			// thực hiện thêm số lượng đã mua vào database
			uptoCountByInProduct(orders);
		} else {
			if (type.toString().equals("PENDING") || type.toString().equals("PREPARE") || type.toString().equals("DELIVERY") || type.toString().equals("SUCCESS")) {
				throw new OrderException("Your order is in status CANCEL cannot switch to state " + type);
			}
			returnStockInProduct(orders.getList());
			orders.setEDelivered(type);
		}
		if (type.equals(EDelivered.DELIVERY)) {
			mailService.sendHtmlToMail(users.getEmail(), "Thông báo đơn hàng của bạn", "<div style=\"border:2px solid #000; display:inline-block; padding: 20px\">\n" +
					  "<h1>Đơn hàng của bạn</h1>\n" +
					  "<h3>Full Name: " + users.getFullName() + "</h3>\n" +
					  "<h3>Email: " + users.getEmail() + "</h3>\n" +
					  "<h3>Address: " + orders.getLocation() + "</h3>\n" +
					  "<h3>Phone: " + orders.getPhone() + "</h3>\n" +
					  "<h3>Total: " + orders.getTotal() + "</h3>\n" +
					  "<h3>Time: " + orders.getDeliveryTime() + "</h3>\n" +
					  "<h3>Trạng thái: Đang giao hàng </h3>\n" +
					  "</div>\n");
		}
		if (type.equals(EDelivered.SUCCESS)) {
			mailService.sendHtmlToMail(users.getEmail(), "Thông báo đơn hàng của bạn", "<div style=\"border:2px solid #000; display:inline-block; padding: 20px\">\n" +
					  "<h1>Đơn hàng của bạn</h1>\n" +
					  "<h3>Full Name: " + users.getFullName() + "</h3>\n" +
					  "<h3>Email: " + users.getEmail() + "</h3>\n" +
					  "<h3>Address: " + orders.getLocation() + "</h3>\n" +
					  "<h3>Phone: " + orders.getPhone() + "</h3>\n" +
					  "<h3>Total: " + orders.getTotal() + "</h3>\n" +
					  "<h3>Time: " + orders.getDeliveryTime() + "</h3>\n" +
					  "<h3>Trạng thái: Giao hàng thành công </h3>\n" +
					  "</div>\n");
		}
		if (type.equals(EDelivered.CANCEL)) {
			mailService.sendHtmlToMail(users.getEmail(), "Thông báo đơn hàng của bạn", "<div style=\"border:2px solid #000; display:inline-block; padding: 20px\">\n" +
					  "<h1>Đơn hàng của bạn</h1>\n" +
					  "<h3>Full Name: " + users.getFullName() + "</h3>\n" +
					  "<h3>Email: " + users.getEmail() + "</h3>\n" +
					  "<h3>Address: " + orders.getLocation() + "</h3>\n" +
					  "<h3>Phone: " + orders.getPhone() + "</h3>\n" +
					  "<h3>Total: " + orders.getTotal() + "</h3>\n" +
					  "<h3>Time: " + orders.getDeliveryTime() + "</h3>\n" +
					  "<h3>Trạng thái: Đã hủy </h3>\n" +
					  "</div>\n");
		}
		return orderMapper.toResponse(orderRepository.save(orders));
	}
	
	@Override
	public List<OrderResponse> getOrders(Authentication authentication) {
		UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
		List<Orders> list = orderRepository.findAllByUsersIdAndStatus(userPrinciple.getId(), true);
		return list.stream().map(item -> orderMapper.toResponse(item))
				  .collect(Collectors.toList());
	}
	
	@Override
	public OrderResponse getOrderById(Long orderId, Authentication authentication) throws OrderException {
		UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
		Optional<Orders> orders = orderRepository.findByIdAndUsersId(orderId, userPrinciple.getId());
		return orders.map(value -> orderMapper.toResponse(value)).orElseThrow(() -> new OrderException("order not found"));
	}
	
	@Override
	public List<CartItemResponse> getCartInOrder(Long orderId, Authentication authentication) throws OrderException {
		UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
		Optional<Orders> orders = orderRepository.findByIdAndUsersId(orderId, userPrinciple.getId());
		if (orders.isPresent()) {
			return orders.get().getList().stream().map(item -> cartItemMapper.toResponse(item)).collect(Collectors.toList());
		}
		throw new OrderException("not found this order");
	}
	
	@Override
	public List<CartItemResponse> getCarts(Authentication authentication) throws OrderException, MessagingException {
		UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
		Optional<Orders> orders = orderRepository.findByUsersIdAndStatus(userPrinciple.getId(), false);
		if (orders.isPresent()) {
			return orders.get().getList().stream().map(item -> cartItemMapper.toResponse(item)).collect(Collectors.toList());
		}
		throw new OrderException("your cart is empty");
	}
	
	@Override
	public CartItemResponse addProductToOrder(Long productDetailId, Authentication authentication) throws UserException, ProductDetailException, CartItemException {
		ProductDetail productDetail = findProductDetailId(productDetailId);
		UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
		Users users = findUserByEmail(userPrinciple.getEmail());
		Optional<Orders> order = orderRepository.findByUsersIdAndStatus(userPrinciple.getId(), false);
		if (!order.isPresent()) {
			Orders orders = Orders.builder()
					  .eDelivered(EDelivered.PENDING)
					  .location(users.getAddress())
					  .phone(users.getPhone())
					  .users(users)
					  .status(false)
					  .build();
			Orders newOrder = orderRepository.save(orders);
			CartItem cartItem = CartItem.builder()
					  .productDetail(productDetail)
					  .orders(newOrder)
					  .quantity(1)
					  .build();
			cartItemRepository.save(cartItem);
			return cartItemMapper.toResponse(cartItem);
		} else {
			// có giỏ hàng
			Optional<CartItem> cartItemOptional = cartItemRepository.findCartItemByOrdersAndProductDetail(order.get(), productDetail);
			CartItem cartItem;
			if (cartItemOptional.isPresent()) {
				// đã có sp trong giỏ hàng
				cartItem = cartItemOptional.get();
				cartItem.setQuantity(cartItem.getQuantity() + 1);
			} else {
				// chưa có sản phẩm trong giỏ hàng
				cartItem = CartItem.builder().orders(order.get()).productDetail(productDetail).quantity(1).build();
			}
			return cartItemMapper.toResponse(cartItemRepository.save(cartItem));
		}
	}
	
	@Override
	public CartItemResponse plusOrderDetail(Long orderDetailId, Authentication authentication) throws UserException, CartItemException {
		UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
		Optional<Orders> orders = orderRepository.findByUsersIdAndStatus(userPrinciple.getId(), false);
		if (orders.isPresent()) {
			Optional<CartItem> cartItem = cartItemRepository.findByOrders(orders.get());
			if (cartItem.isPresent()) {
				if (cartItem.get().getQuantity() == cartItem.get().getProductDetail().getStock()) {
					throw new CartItemException("i don't have this product detail");
				}
				cartItem.get().setQuantity(cartItem.get().getQuantity() + 1);
				return cartItemMapper.toResponse(cartItemRepository.save(cartItem.get()));
			}
			throw new CartItemException("you don't have this cart item");
		}
		throw new UserException("you don't have this order");
	}
	
	@Override
	public CartItemResponse minusOrderDetail(Long orderDetailId, Authentication authentication) throws OrderException, CartItemException {
		UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
		Optional<Orders> orders = orderRepository.findByUsersIdAndStatus(userPrinciple.getId(), false);
		if (orders.isPresent()) {
			Optional<CartItem> cartItem = cartItemRepository.findByOrders(orders.get());
			if (cartItem.isPresent()) {
				if (cartItem.get().getQuantity() == 1) {
					cartItemRepository.delete(cartItem.get());
					cartItem.get().setQuantity(0);
					return cartItemMapper.toResponse(cartItem.get());
				} else {
					cartItem.get().setQuantity(cartItem.get().getQuantity() - 1);
					return cartItemMapper.toResponse(cartItemRepository.save(cartItem.get()));
				}
			}
			throw new CartItemException("you don't have this cart item");
		}
		throw new OrderException("you don't have this order");
	}
	
	@Override
	public CartItemResponse removeOrderDetail(Long orderDetailId, Authentication authentication) throws CartItemException, OrderException {
		UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
		Optional<Orders> orders = orderRepository.findByUsersIdAndStatus(userPrinciple.getId(), false);
		if (orders.isPresent()) {
			Optional<CartItem> cartItem = cartItemRepository.findByOrders(orders.get());
			if (cartItem.isPresent()) {
				cartItemRepository.deleteById(orderDetailId);
				cartItem.get().setQuantity(0);
				return cartItemMapper.toResponse(cartItem.get());
			}
			throw new CartItemException("you don't have this cart item");
		}
		throw new OrderException("you don't have this order");
	}
	
	@Override
	public List<CartItemResponse> removeAllInYourCart(Authentication authentication) throws OrderException {
		UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
		Optional<Orders> orders = orderRepository.findByUsersIdAndStatus(userPrinciple.getId(), false);
		if (orders.isPresent()) {
			cartItemRepository.resetCartItemByOrderId(orders.get());
			return orders.get().getList().stream()
					  .map(item -> cartItemMapper.toResponse(item))
					  .collect(Collectors.toList());
		}
		throw new OrderException("You cart is empty");
	}
	
	@Override
	public OrderResponse checkoutYourCart(CheckoutRequest checkoutRequest, Authentication authentication) throws UserException, OrderException, CouponException, MessagingException {
		UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
		Optional<Orders> orders = orderRepository.findByUsersIdAndStatus(userPrinciple.getId(), false);
		if (orders.isPresent()) {
			Orders newOrder = orders.get();
			if (newOrder.getList().isEmpty()) {
				throw new OrderException("your cart is empty");
			}
			for (CartItem c : newOrder.getList()) {
				c.setPrice(c.getProductDetail().getProduct().getPrice());
			}
			newOrder.setDeliveryTime(new Date());
			newOrder.setStatus(true);
			newOrder.setEDelivered(EDelivered.PREPARE);
			Coupon coupon = null;
			if (checkoutRequest.getCouponId() != null) {
				coupon = findCouponById(checkoutRequest.getCouponId());
			}
			if (checkoutRequest.getAddress() == null || checkoutRequest.getPhone() == null) {
				if (userPrinciple.getAddress() == null || userPrinciple.getPhone() == null) {
					throw new UserException("you must be update your info");
				}
				// check out with my information
				newOrder.setLocation(userPrinciple.getAddress());
				newOrder.setPhone(userPrinciple.getPhone());
				newOrder.setTotal(newOrder.getList().stream().map(item -> item.getPrice() * item.getQuantity()).reduce((double) 0, Double::sum));
			} else {
				// check out with other address or other phone
				newOrder.setLocation(checkoutRequest.getAddress());
				newOrder.setPhone(checkoutRequest.getPhone());
				newOrder.setTotal(newOrder.getList().stream().map(item -> item.getPrice() * item.getQuantity()).reduce((double) 0, Double::sum));
			}
			if (coupon != null) {
				boolean check = checkDateInCoupon(coupon);
				if (check) {
					if (coupon.getStock() == 0) {
						coupon.setStatus(false);
						couponRepository.save(coupon);
						throw new CouponException("i don't have this coupon");
					}
					orders.get().setCoupon(coupon);
					orders.get().setTotal(newOrder.getTotal() - (newOrder.getTotal() * coupon.getPercent()));
					coupon.setStock(coupon.getStock() - 1);
					couponRepository.save(coupon);
				} else {
					throw new CouponException("Discount code has expired");
				}
			}
			// thực hiện trừ stock ở trong product detail
			minusStockInProduct(newOrder.getList());
			mailService.sendHtmlToMail(userPrinciple.getEmail(), "Thông báo đơn hàng của bạn", "<div style=\"border:2px solid #000; display:inline-block; padding: 20px\">\n" +
					  "<h1>Đơn hàng của bạn</h1>\n" +
					  "<h3>Full Name: " + userPrinciple.getFullName() + "</h3>\n" +
					  "<h3>Email: " + userPrinciple.getEmail() + "</h3>\n" +
					  "<h3>Address: " + newOrder.getLocation() + "</h3>\n" +
					  "<h3>Phone: " + newOrder.getPhone() + "</h3>\n" +
					  "<h3>Total: " + newOrder.getTotal() + "</h3>\n" +
					  "<h3>Time: " + newOrder.getDeliveryTime() + "</h3>\n" +
					  "<h3>Trạng thái: Chuẩn bị hàng</h3>\n" +
					  "</div>\n");
			return orderMapper.toResponse(orderRepository.save(newOrder));
		}
		throw new OrderException("your cart is empty");
	}
	
	@Override
	public OrderResponse cancelOrder(Long orderId, Authentication authentication) throws OrderException, UserException, MessagingException {
		UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
		Optional<Orders> orders = orderRepository.findByIdAndUsersId(orderId, userPrinciple.getId());
		if (orders.isPresent()) {
			orders.get().setDeliveryTime(new Date());
			mailService.sendHtmlToMail(userPrinciple.getEmail(), "Thông báo đơn hàng của bạn", "<div style=\"border:2px solid #000; display:inline-block; padding: 20px\">\n" +
					  "<h1>Đơn hàng của bạn</h1>\n" +
					  "<h3>Full Name: " + userPrinciple.getFullName() + "</h3>\n" +
					  "<h3>Email: " + userPrinciple.getEmail() + "</h3>\n" +
					  "<h3>Address: " + orders.get().getLocation() + "</h3>\n" +
					  "<h3>Phone: " + orders.get().getPhone() + "</h3>\n" +
					  "<h3>Total: " + orders.get().getTotal() + "</h3>\n" +
					  "<h3>Time: " + orders.get().getDeliveryTime() + "</h3>\n" +
					  "<h3>Trạng thái: Đã Hủy</h3>\n" +
					  "</div>\n");
			returnStockInProduct(orders.get().getList());
			return changeDelivery("cancel", orderId);
		}
		throw new UserException("Un permission");
	}
	
	public void uptoCountByInProduct(Orders orders) {
		for (CartItem c : orders.getList()) {
			c.getProductDetail().getProduct().setCountBuy(c.getProductDetail().getProduct().getCountBuy() + c.getQuantity());
			productRepository.save(c.getProductDetail().getProduct());
		}
	}
	
	
	public boolean checkDateInCoupon(Coupon coupon) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate startDateCoupon = LocalDate.parse(coupon.getStartDate().toString(), formatter);
		LocalDate endDateCoupon = LocalDate.parse(coupon.getEndDate().toString(), formatter);
		LocalDate timeNow = LocalDate.parse(new Date().toString(), formatter);
		return !timeNow.isAfter(endDateCoupon) && !timeNow.isBefore(startDateCoupon);
	}
	
	public void returnStockInProduct(List<CartItem> cartItems) {
		for (CartItem c : cartItems) {
			c.getProductDetail().setStock(c.getProductDetail().getStock() + c.getQuantity());
			productDetailRepository.save(c.getProductDetail());
		}
	}
	
	public void minusStockInProduct(List<CartItem> cartItems) throws OrderException {
		for (CartItem c : cartItems) {
			if (c.getQuantity() > c.getProductDetail().getStock()) {
				throw new OrderException("i don't have enough");
			}
			c.getProductDetail().setStock(c.getProductDetail().getStock() - c.getQuantity());
			productDetailRepository.save(c.getProductDetail());
		}
	}
	
	public Coupon findCouponById(Long couponId) throws CouponException {
		Optional<Coupon> optionalCoupon = couponRepository.findById(couponId);
		return optionalCoupon.orElseThrow(() -> new CouponException("coupon not found"));
	}
	
	public ProductDetail findProductDetailId(Long productDetailId) throws ProductDetailException {
		Optional<ProductDetail> optionalProductDetail = productDetailRepository.findById(productDetailId);
		return optionalProductDetail.orElseThrow(() -> new ProductDetailException("product detail not found"));
	}
	
	public Orders findOrderById(Long id) throws OrderException {
		Optional<Orders> optionalOrders = orderRepository.findById(id);
		return optionalOrders.orElseThrow(() -> new OrderException("order not found"));
	}
	
	public EDelivered findDeliveryByInput(String typeDelivery) throws OrderException {
		switch (typeDelivery) {
			case "pending":
				return EDelivered.PENDING;
			case "prepare":
				return EDelivered.PREPARE;
			case "delivery":
				return EDelivered.DELIVERY;
			case "success":
				return EDelivered.SUCCESS;
			case "cancel":
				return EDelivered.CANCEL;
			default:
				throw new OrderException("delivery not found");
		}
	}
	
	public Users findUserByEmail(String email) throws UserException {
		Optional<Users> optionalUsers = userRepository.findByEmail(email);
		return optionalUsers.orElseThrow(() -> new UserException("user not found"));
	}
	
}
