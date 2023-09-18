package ra.service.orders;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import ra.exception.OrderException;
import ra.exception.ProductException;
import ra.exception.UserException;
import ra.model.dto.request.OrderRequest;
import ra.model.dto.response.OrderResponse;

import java.util.List;
import java.util.Optional;

public interface IOrderService {
	
	Page<OrderResponse> findAll(Pageable pageable, Optional<String> phone);
	
	OrderResponse findById(Long id) throws OrderException;
	
	OrderResponse save(OrderRequest orderRequest);
	
	OrderResponse update(OrderRequest orderRequest, Long id);
	
	OrderResponse changeStatus(Long id) throws OrderException;
	
	List<OrderResponse> getOrders(Authentication authentication) throws UserException;
	
	OrderResponse addProductToOrder(Long productId, Authentication authentication) throws ProductException, UserException;
	
	OrderResponse changeDelivery(String typeDelivery, Long id) throws OrderException;
	
	OrderResponse buyProductInCartUser(Long productId, Long userId) throws ProductException, UserException;
	
	OrderResponse addProductToOrderUser(Long productId, Long orderId);
	
}
