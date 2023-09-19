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
import ra.model.dto.request.OrderRequest;
import ra.model.dto.response.CartItemResponse;
import ra.model.dto.response.OrderResponse;
import ra.repository.*;

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
	private IProductRepository productRepository;
	@Autowired
	private IProductDetailRepository productDetailRepository;
	
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
			orders.setEDelivered(type);
		}
		return orderMapper.toResponse(orderRepository.save(orders));
	}
	
	@Override
	public List<OrderResponse> getOrders(Authentication authentication) throws UserException {
		Users users = findUserByAuthentication(authentication);
		return users.getOrders().stream().filter(Orders::isStatus).map(item -> orderMapper.toResponse(item)).collect(Collectors.toList());
	}
	
	@Override
	public List<CartItemResponse> getCarts(Authentication authentication) throws UserException, OrderException {
		Users users = findUserByAuthentication(authentication);
		Orders orders = findCartUser(users);
		if (orders != null) {
			return orders.getList().stream().map(item -> cartItemMapper.toResponse(item)).collect(Collectors.toList());
		}
		throw new OrderException("your cart is empty");
	}
	
	@Override
	public CartItemResponse addProductToOrder(Long productDetailId, Authentication authentication) throws ProductException, UserException, ProductDetailException, CartItemException {
		ProductDetail productDetail = findProductDetailId(productDetailId);
		Users users = findUserByAuthentication(authentication);
//		if (users.getAddress() == null || users.getPhone() == null) {
//			throw new UserException("you must be update your info");
//		}
		Orders order = findCartUser(users);
		if (order == null) {
			Orders orders = Orders.builder()
					  .eDelivered(EDelivered.PENDING)
					  .deliveryTime(new Date())
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
			Optional<CartItem> cartItemOptional = cartItemRepository.findCartItemByOrdersAndProductDetail(order, productDetail);
			if (cartItemOptional.isPresent()) {
				// đã có sp trong giỏ hàng
				CartItem cartItem = cartItemOptional.get();
				cartItem.setQuantity(cartItem.getQuantity() + 1);
				
				return cartItemMapper.toResponse(cartItemRepository.save(cartItem));
			} else {
				// chưa có sản phẩm trong giỏ hàng
				CartItem cartItem = CartItem.builder().orders(order).productDetail(productDetail).quantity(1).build();
				return cartItemMapper.toResponse(cartItemRepository.save(cartItem));
			}
		}
	}
	
	@Override
	public CartItemResponse plusOrderDetail(Long orderDetailId, Authentication authentication) throws UserException, OrderException, CartItemException {
		Users users = findUserByAuthentication(authentication);
		Orders orders = findCartUser(users);
		if (orders != null) {
			CartItem cartItem = findCartItemById(orderDetailId);
			if (cartItem.getQuantity() == cartItem.getProductDetail().getStock()) {
				throw new CartItemException("i don't have this product detail");
			}
			cartItem.setQuantity(cartItem.getQuantity() + 1);
			return cartItemMapper.toResponse(cartItemRepository.save(cartItem));
		}
		throw new UserException("you don't have this order");
	}
	
	@Override
	public CartItemResponse minusOrderDetail(Long orderDetailId, Authentication authentication) throws UserException, OrderException, CartItemException {
		Users users = findUserByAuthentication(authentication);
		Orders orders = findCartUser(users);
		if (orders != null) {
			CartItem cartItem = findCartItemById(orderDetailId);
			if (cartItem.getQuantity() == 1) {
				cartItemRepository.delete(cartItem);
				cartItem.setQuantity(0);
				return cartItemMapper.toResponse(cartItem);
			} else {
				cartItem.setQuantity(cartItem.getQuantity() - 1);
				return cartItemMapper.toResponse(cartItemRepository.save(cartItem));
			}
		}
		throw new OrderException("you don't have this order");
	}
	
	@Override
	public CartItemResponse removeOrderDetail(Long orderDetailId, Authentication authentication) throws UserException, CartItemException, OrderException {
		Users users = findUserByAuthentication(authentication);
		Orders orders = findCartUser(users);
		if (orders != null) {
			CartItem cartItem = findCartItemById(orderDetailId);
			cartItemRepository.deleteById(orderDetailId);
			cartItem.setQuantity(0);
			return cartItemMapper.toResponse(cartItem);
		}
		throw new OrderException("you don't have this order");
	}
	
	@Override
	public List<CartItemResponse> removeAllInYourCart(Authentication authentication) throws UserException, OrderException {
		// đang lỗi
		Users users = findUserByAuthentication(authentication);
		Orders orders = findCartUser(users);
		if (orders != null) {
//			cartItemRepository.deleteAllByOrders(orders);
			cartItemRepository.resetCartItemByOrderId(orders);
			return orders.getList().stream().map(item -> cartItemMapper.toResponse(item)).collect(Collectors.toList());
		}
		throw new OrderException("You cart is empty");
	}
	
	@Override
	public OrderResponse checkoutYourCart(Authentication authentication) {
		return null;
	}
	
	public CartItem findCartItemById(Long orderDetailId) throws CartItemException {
		Optional<CartItem> optionalCartItem = cartItemRepository.findById(orderDetailId);
		return optionalCartItem.orElseThrow(() -> new CartItemException("cart item not found"));
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
	
	public Orders findCartUser(Users users) {
		for (Orders o : users.getOrders()) {
			if (!o.isStatus()) {
				return o;
			}
		}
		return null;
	}
	
	public Users findUserByAuthentication(Authentication authentication) throws UserException {
		if (authentication != null && authentication.isAuthenticated()) {
			String username = authentication.getName();
			return findUserByEmail(username);
		}
		throw new UserException("Un Authentication");
	}
	
	public Users findUserByEmail(String email) throws UserException {
		Optional<Users> optionalUsers = userRepository.findByEmail(email);
		return optionalUsers.orElseThrow(() -> new UserException("user not found"));
	}
	
}
