package ra.service.favourite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ra.exception.ProductException;
import ra.exception.UserException;
import ra.mapper.product.ProductMapper;
import ra.model.domain.Product;
import ra.model.domain.Users;
import ra.model.dto.response.ProductResponse;
import ra.repository.IProductRepository;
import ra.repository.IUserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavouriteService implements IFavouriteService {
	
	@Autowired
	private IUserRepository userRepository;
	@Autowired
	private IProductRepository productRepository;
	@Autowired
	private ProductMapper productMapper;
	
	@Override
	public List<ProductResponse> getFavourite(Authentication authentication) throws UserException {
		Users users = findUserByAuthentication(authentication);
		return users.getFavourites().stream()
				  .map(item -> productMapper.toResponse(item))
				  .collect(Collectors.toList());
	}
	
	@Override
	public List<ProductResponse> addProductToFavourite(Long productId, Authentication authentication) throws UserException, ProductException {
		Product product = findProductById(productId);
		Users users = findUserByAuthentication(authentication);
		boolean check = users.getFavourites().contains(product);
		if(check) {
			throw new ProductException("you have product in your favourite");
		}
		users.getFavourites().add(product);
		userRepository.save(users);
		return findUserByAuthentication(authentication).getFavourites().stream()
				  .map(item -> productMapper.toResponse(item))
				  .collect(Collectors.toList());
	}
	
	@Override
	public List<ProductResponse> removeProductInFavourite(Long productId, Authentication authentication) throws UserException, ProductException {
		Users users = findUserByAuthentication(authentication);
		Product product = findProductById(productId);
		if (!users.getFavourites().contains(product)) {
			throw new UserException("You no has product in your favourite");
		}
		users.getFavourites().remove(product);
		return userRepository.save(users).getFavourites().stream()
				  .map(item -> productMapper.toResponse(item))
				  .collect(Collectors.toList());
	}
	
	
	public Product findProductById(Long productId) throws ProductException {
		Optional<Product> optionalProduct = productRepository.findById(productId);
		return optionalProduct.orElseThrow(() -> new ProductException("product not found"));
	}
	
	public Users findUserByAuthentication(Authentication authentication) throws UserException {
		if (authentication != null && authentication.isAuthenticated()) {
			String username = authentication.getName();
			return findUserByUserName(username);
		}
		throw new UserException("Un Authentication");
	}
	
	public Users findUserByUserName(String email) throws UserException {
		Optional<Users> optionalUsers = userRepository.findByEmail(email);
		return optionalUsers.orElseThrow(() -> new UserException("user not found"));
	}
	
}
