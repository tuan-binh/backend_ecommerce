package ra.service.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ra.exception.CouponException;
import ra.exception.OrderException;
import ra.exception.ProductException;
import ra.exception.UserException;
import ra.mapper.orders.OrderMapper;
import ra.model.domain.*;
import ra.model.dto.request.OrderRequest;
import ra.model.dto.response.OrderResponse;
import ra.repository.IOrderRepository;
import ra.repository.IProductRepository;
import ra.repository.IUserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {
	
	@Autowired
	private IOrderRepository orderRepository;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private IUserRepository userRepository;
	@Autowired
	private IProductRepository productRepository;
	
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
		return users.getOrders().stream().map(item -> orderMapper.toResponse(item)).collect(Collectors.toList());
	}
	
	@Override
	public OrderResponse addProductToOrder(Long productId, Authentication authentication) throws ProductException, UserException {
		Product product = findProductById(productId);
		Users users = findUserByAuthentication(authentication);
		if (users.getAddress() == null || users.getPhone() == null) {
			throw new UserException("you must be update your info");
		}
		Orders order = findOrderPending(users);
		if (order == null) {
			List<CartItem> list = new ArrayList<>();
			list.add(CartItem.builder()
					  .product(product)
					  .price(product.getPrice())
					  .quantity(1)
					  .status(true)
					  .build());
			Orders newOrder = Orders.builder()
					  .eDelivered(EDelivered.PENDING)
					  .deliveryTime(new Date())
					  .location(users.getAddress())
					  .phone(users.getPhone())
					  .total(product.getPrice())
					  .list(list)
					  .status(false)
					  .build();
			return orderMapper.toResponse(orderRepository.save(newOrder));
		}
		
		return null;
	}
	
	@Override
	public OrderResponse buyProductInCartUser(Long productId, Long userId) throws ProductException, UserException {
		Users users = findUserById(userId);
		Product product = findProductById(productId);


//		List<CartItem> list = new ArrayList<>();
//		list.add(CartItem.builder().product(product).price(product.getPrice()).quantity(1).status(true).build());
//		Orders orders = Orders.builder()
//				  .eDelivered(EDelivered.PENDING)
//				  .deliveryTime(new Date())
//				  .location(users.getAddress())
//				  .phone(users.getPhone())
//				  .total(product.getPrice())
//				  .list(list)
//				  .users(users)
//				  .status(false)
//				  .build();
//		users.getOrders().add(orders);
//		userRepository.save(users);
//		return orderMapper.toResponse(orderRepository.save(orders));
		
		return null;
	}
	
	@Override
	public OrderResponse addProductToOrderUser(Long productId, Long orderId) {
		return null;
	}
	
	public Orders findOrderStatusPending(Users users) throws OrderException {
		for (Orders u : users.getOrders()) {
			if (u.getEDelivered().equals(EDelivered.PENDING)) {
				return u;
			}
		}
		throw new OrderException("order not found");
	}
	
	public Users findUserById(Long userId) throws UserException {
		Optional<Users> optionalUsers = userRepository.findById(userId);
		return optionalUsers.orElseThrow(() -> new UserException("user not found"));
	}
	
	public Product findProductById(Long productId) throws ProductException {
		Optional<Product> optionalProduct = productRepository.findById(productId);
		return optionalProduct.orElseThrow(() -> new ProductException("product not found"));
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
	
	public Orders findOrderPending(Users users) {
		for (Orders o : users.getOrders()) {
			if (o.getEDelivered().equals(EDelivered.PENDING)) {
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
