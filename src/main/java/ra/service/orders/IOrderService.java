package ra.service.orders;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import ra.exception.*;
import ra.model.dto.request.CheckoutRequest;
import ra.model.dto.request.OrderRequest;
import ra.model.dto.response.CartItemResponse;
import ra.model.dto.response.OrderResponse;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Optional;

public interface IOrderService {
	
	Page<OrderResponse> findAll(Pageable pageable, Optional<String> phone);
	
	OrderResponse findById(Long id) throws OrderException;
	
	OrderResponse save(OrderRequest orderRequest) throws CouponException;
	
	OrderResponse update(OrderRequest orderRequest, Long id) throws CouponException;
	
	OrderResponse changeStatus(Long id) throws OrderException;
	
	List<OrderResponse> getOrders(Authentication authentication) throws UserException;
	
	OrderResponse getOrderById(Long orderId, Authentication authentication) throws OrderException;
	
	List<CartItemResponse> getCarts(Authentication authentication) throws UserException, OrderException, MessagingException;
	
	CartItemResponse addProductToOrder(Long productDetailId, Authentication authentication) throws ProductException, UserException, ProductDetailException, CartItemException;
	
	CartItemResponse plusOrderDetail(Long orderDetailId, Authentication authentication) throws OrderException, CartItemException, UserException;
	
	CartItemResponse minusOrderDetail(Long orderDetailId, Authentication authentication) throws OrderException, CartItemException;
	
	CartItemResponse removeOrderDetail(Long orderDetailId, Authentication authentication) throws UserException, CartItemException, OrderException;
	
	List<CartItemResponse> removeAllInYourCart(Authentication authentication) throws UserException, OrderException;
	
	OrderResponse checkoutYourCart(CheckoutRequest checkoutRequest, Authentication authentication) throws UserException, OrderException, CouponException, MessagingException;
	
	OrderResponse cancelOrder(Long orderId, Authentication authentication) throws OrderException, UserException, MessagingException;
	
	OrderResponse changeDelivery(String typeDelivery, Long orderId) throws OrderException;
	
}
