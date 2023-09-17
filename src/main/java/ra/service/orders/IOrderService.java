package ra.service.orders;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.exception.OrderException;
import ra.exception.ProductException;
import ra.exception.UserException;
import ra.model.dto.request.OrderRequest;
import ra.model.dto.response.OrderResponse;

import java.util.Optional;

public interface IOrderService {
	
	Page<OrderResponse> findAll(Pageable pageable, Optional<String> phone);
	
	OrderResponse findById(Long id) throws OrderException;
	
	OrderResponse save(OrderRequest orderRequest);
	
	OrderResponse update(OrderRequest orderRequest, Long id);
	
	OrderResponse changeStatus(Long id) throws OrderException;
	
	OrderResponse changeDelivery(String typeDelivery, Long id) throws OrderException;
	
	OrderResponse BuyProductInCartUser(Long productId, Long userId) throws ProductException, UserException;
	
}
