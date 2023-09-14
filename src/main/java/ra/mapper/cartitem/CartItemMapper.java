package ra.mapper.cartitem;

import org.springframework.stereotype.Component;
import ra.mapper.IGenericMapper;
import ra.model.domain.CartItem;
import ra.model.dto.request.CartItemRequest;
import ra.model.dto.response.CartItemResponse;

@Component
public class CartItemMapper implements IGenericMapper<CartItem, CartItemRequest, CartItemResponse> {
	@Override
	public CartItem toEntity(CartItemRequest cartItemRequest) {
		return CartItem.builder()
				  .product(cartItemRequest.getProduct())
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
}
