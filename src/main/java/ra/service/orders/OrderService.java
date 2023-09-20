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
	public OrderResponse changeDelivery(String typeDelivery, Long id) throws OrderException {
		EDelivered type = findDeliveryByInput(typeDelivery);
		Orders orders = findOrderById(id);
		if (orders.getEDelivered().toString().equals("PENDING")) {
			if (type.toString().equals("DELIVERY") || type.toString().equals("SUCCESS")) {
				throw new OrderException("You are in the status of waiting for confirmation");
			}
			orders.setEDelivered(type);
		} else if (orders.getEDelivered().toString().equals("PREPARE")) {
			if (type.toString().equals("PENDING") || type.toString().equals("SUCCESS")) {
				throw new OrderException("You are in preparation mode");
			}
			orders.setEDelivered(type);
		} else if (orders.getEDelivered().toString().equals("DELIVERY")) {
			if (type.toString().equals("PENDING") || type.toString().equals("PREPARE")) {
				throw new OrderException("You are in the delivery status");
			}
			orders.setEDelivered(type);
		} else if (orders.getEDelivered().toString().equals("SUCCESS")) {
			if (type.toString().equals("PENDING") || type.toString().equals("PREPARE") || type.toString().equals("DELIVERY")) {
				throw new OrderException("You are in the successful delivery status");
			}
			orders.setEDelivered(type);
		} else {
			if (type.toString().equals("PENDING") || type.toString().equals("PREPARE") || type.toString().equals("DELIVERY") || type.toString().equals("SUCCESS")) {
				throw new OrderException("You are in the order cancellation status");
			}
			returnStockInProduct(orders.getList());
			orders.setEDelivered(type);
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
		if (userPrinciple.getAddress() == null || userPrinciple.getPhone() == null) {
			throw new UserException("you must be update your info");
		}
		if (orders.isPresent()) {
			if (orders.get().getList().isEmpty()) {
				throw new OrderException("your cart is empty");
			}
			for (CartItem c : orders.get().getList()) {
				c.setPrice(c.getProductDetail().getProduct().getPrice());
			}
			orders.get().setDeliveryTime(new Date());
			orders.get().setStatus(true);
			orders.get().setEDelivered(EDelivered.PREPARE);
			Coupon coupon = null;
			if (checkoutRequest.getCouponId() != null) {
				coupon = findCouponById(checkoutRequest.getCouponId());
			}
			if (checkoutRequest.getAddress() == null || checkoutRequest.getPhone() == null) {
				// check out with my information
				orders.get().setLocation(userPrinciple.getAddress());
				orders.get().setPhone(userPrinciple.getPhone());
				orders.get().setTotal(orders.get().getList().stream().map(item -> item.getPrice() * item.getQuantity()).reduce((double) 0, Double::sum));
			} else {
				// check out with other address or other phone
				orders.get().setLocation(checkoutRequest.getAddress());
				orders.get().setPhone(checkoutRequest.getPhone());
				orders.get().setTotal(orders.get().getList().stream().map(item -> item.getPrice() * item.getQuantity()).reduce((double) 0, Double::sum));
			}
			if (coupon != null) {
				boolean check = checkDateInCoupon(coupon);
				if (check) {
					orders.get().setCoupon(coupon);
					orders.get().setTotal(orders.get().getTotal() - (orders.get().getTotal() * coupon.getPercent()));
					coupon.setStock(coupon.getStock() - 1);
					couponRepository.save(coupon);
				} else {
					throw new CouponException("Discount code has expired");
				}
			}
			// thực hiện trừ stock ở trong product detail
			minusStockInProduct(orders.get().getList());
			mailService.sendHtmlToMail(userPrinciple.getEmail(), "Thông báo đơn hàng của bạn", "<div style=\"border:2px solid #000; display:inline-block; padding: 20px\">\n" +
					  "<h1>Đơn hàng của bạn</h1>\n" +
					  "<h3>Full Name: " + userPrinciple.getFullName() + "</h3>\n" +
					  "<h3>Email: " + userPrinciple.getEmail() + "</h3>\n" +
					  "<h3>Address: " + orders.get().getLocation() + "</h3>\n" +
					  "<h3>Phone: " + orders.get().getPhone() + "</h3>\n" +
					  "<h3>Total: " + orders.get().getTotal() + "</h3>\n" +
					  "<h3>Time: " + orders.get().getDeliveryTime() + "</h3>\n" +
					  "<h3>Trạng thái: Chuẩn bị hàng</h3>\n" +
					  "</div>\n");
			return orderMapper.toResponse(orderRepository.save(orders.get()));
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
			c.getProductDetail().getProduct().setCountBuy(c.getQuantity());
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
