package ra.service.cartitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.exception.CartItemException;
import ra.mapper.cartitem.CartItemMapper;
import ra.model.domain.CartItem;
import ra.repository.ICartItemRepository;

import java.util.Optional;

@Service
public class CartItemService implements ICartItemService {
	
	@Autowired
	private ICartItemRepository cartItemRepository;
	
	@Override
	public Page<CartItem> findAll(Pageable pageable) {
		return cartItemRepository.findAll(pageable);
		
	}
	
	@Override
	public CartItem findById(Long id) throws CartItemException {
		Optional<CartItem> optionalCartItem = cartItemRepository.findById(id);
		return optionalCartItem.orElseThrow(() -> new CartItemException("cartItem not found"));
	}
	
	@Override
	public CartItem save(CartItem cartItem) {
		return cartItemRepository.save(cartItem);
	}
	
	@Override
	public CartItem update(CartItem cartItem, Long id) {
		cartItem.setId(id);
		return cartItemRepository.save(cartItem);
	}
	
	@Override
	public CartItem delete(Long id) throws CartItemException {
		Optional<CartItem> optionalCartItem = cartItemRepository.findById(id);
		if (optionalCartItem.isPresent()) {
			cartItemRepository.deleteById(id);
			return optionalCartItem.get();
		}
		throw new CartItemException("cartItem not found");
	}
}
