package ra.service.cartitem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.exception.CartItemException;
import ra.model.domain.CartItem;

import java.util.List;

public interface ICartItemService {
	
	Page<CartItem> findAll(Pageable pageable);
	
	CartItem findById(Long id) throws CartItemException;
	
	CartItem save(CartItem cartItem);
	
	CartItem update(CartItem cartItem, Long id);
	
	CartItem delete(Long id) throws CartItemException;
	
}
