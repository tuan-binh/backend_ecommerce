package ra.service.favourite;

import org.springframework.security.core.Authentication;
import ra.exception.ProductException;
import ra.exception.UserException;
import ra.model.dto.response.ProductResponse;

import java.util.List;

public interface IFavouriteService {
	
	List<ProductResponse> addProductToFavourite(Long productId, Authentication authentication) throws UserException, ProductException;
	
	List<ProductResponse> removeProductInFavourite(Long productId, Authentication authentication) throws UserException, ProductException;
	
	List<ProductResponse> getFavourite(Authentication authentication) throws UserException;
}
