package ra.mapper.cartitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ra.exception.OrderException;
import ra.exception.ProductException;
import ra.mapper.IGenericMapper;
import ra.model.domain.CartItem;
import ra.model.domain.Orders;
import ra.model.domain.Product;
import ra.model.dto.request.CartItemRequest;
import ra.model.dto.response.CartItemResponse;
import ra.repository.IOrderRepository;
import ra.repository.IProductRepository;
import ra.service.orders.OrderService;
import ra.service.product.ProductService;

import java.util.Optional;

@Component
public class CartItemMapper implements IGenericMapper<CartItem, CartItemRequest, CartItemResponse> {
	
	@Autowired
	private IProductRepository productRepository;
	@Autowired
	private IOrderRepository orderRepository;
	
	@Override
	public CartItem toEntity(CartItemRequest cartItemRequest) throws ProductException, OrderException {
		return CartItem.builder()
				  .product(findProductById(cartItemRequest.getProductId()))
				  .orders(findOrderById(cartItemRequest.getOrderId()))
				  .price(cartItemRequest.getPrice())
				  .quantity(cartItemRequest.getQuantity())
				  .status(cartItemRequest.isStatus())
				  .build();
	}
	
	@Override
	public CartItemResponse toResponse(CartItem cartItem) {
		return CartItemResponse.builder()
				  .id(cartItem.getId())
				  .product(cartItem.getProduct())
				  .orders(cartItem.getOrders())
				  .price(cartItem.getPrice())
				  .quantity(cartItem.getQuantity())
				  .status(cartItem.isStatus())
				  .build();
	}
	
	public Product findProductById(Long productId) throws ProductException {
		Optional<Product> optionalProduct = productRepository.findById(productId);
		return optionalProduct.orElseThrow(() -> new ProductException("product not found"));
	}
	
	public Orders findOrderById(Long orderId) throws OrderException {
		Optional<Orders> optionalOrders = orderRepository.findById(orderId);
		return optionalOrders.orElseThrow(() -> new OrderException("order not found"));
	}
	
}
