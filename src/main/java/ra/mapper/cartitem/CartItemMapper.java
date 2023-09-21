package ra.mapper.cartitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ra.exception.OrderException;
import ra.exception.ProductDetailException;
import ra.exception.ProductException;
import ra.mapper.IGenericMapper;
import ra.model.domain.CartItem;
import ra.model.domain.Orders;
import ra.model.domain.ProductDetail;
import ra.model.dto.request.CartItemRequest;
import ra.model.dto.response.CartItemResponse;
import ra.repository.IOrderRepository;
import ra.repository.IProductDetailRepository;

import java.util.Optional;

@Component
public class CartItemMapper implements IGenericMapper<CartItem, CartItemRequest, CartItemResponse> {
	
	@Autowired
	private IOrderRepository orderRepository;
	@Autowired
	private IProductDetailRepository productDetailRepository;
	
	@Override
	public CartItem toEntity(CartItemRequest cartItemRequest) throws ProductException, OrderException, ProductDetailException {
		return CartItem.builder()
				  .productDetail(findProductDetail(cartItemRequest.getProductDetailId()))
				  .orders(findOrderById(cartItemRequest.getOrderId()))
				  .price(cartItemRequest.getPrice())
				  .quantity(cartItemRequest.getQuantity())
				  .build();
	}
	
	@Override
	public CartItemResponse toResponse(CartItem cartItem) {
		return CartItemResponse.builder()
				  .id(cartItem.getId())
				  .productName(cartItem.getProductDetail().getProduct().getProductName())
				  .url(cartItem.getProductDetail().getProduct().getImageActive())
				  .price(cartItem.getProductDetail().getProduct().getPrice())
				  .color(cartItem.getProductDetail().getColor().getColorName())
				  .size(cartItem.getProductDetail().getSize().getSizeName())
				  .quantity(cartItem.getQuantity())
				  .build();
	}
	
	public ProductDetail findProductDetail(Long productDetailId) throws ProductDetailException {
		Optional<ProductDetail> optionalProductDetail = productDetailRepository.findById(productDetailId);
		return optionalProductDetail.orElseThrow(() -> new ProductDetailException("product detail not found"));
	}
	
	public Orders findOrderById(Long orderId) throws OrderException {
		Optional<Orders> optionalOrders = orderRepository.findById(orderId);
		return optionalOrders.orElseThrow(() -> new OrderException("order not found"));
	}
	
}
