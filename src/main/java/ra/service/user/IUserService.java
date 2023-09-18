package ra.service.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import ra.exception.ProductException;
import ra.exception.RateException;
import ra.exception.RoleException;
import ra.exception.UserException;
import ra.model.domain.Product;
import ra.model.domain.Users;
import ra.model.dto.request.RateRequest;
import ra.model.dto.request.UserLogin;
import ra.model.dto.request.UserRegister;
import ra.model.dto.response.JwtResponse;
import ra.model.dto.response.ProductResponse;
import ra.model.dto.response.UserResponse;

import java.util.List;
import java.util.Optional;

public interface IUserService {
	
	Page<UserResponse> findAll(Pageable pageable, Optional<String> fullName);
	
	Optional<Users> findByEmail(String email);
	
	Users save(UserRegister userRegister) throws UserException, RoleException;
	
	UserResponse findById(Long id) throws UserException;
	
	JwtResponse login(UserLogin userLogin) throws UserException;
	
	UserResponse changeStatus(Long id) throws UserException;
	
	List<ProductResponse> addProductToFavourite(Long productId, Authentication authentication) throws UserException, ProductException;
	
	List<ProductResponse> removeProductInFavourite(Long productId, Authentication authentication) throws UserException, ProductException;
	
	ProductResponse rateProductByUser(RateRequest rateRequest, Long productId, Authentication authentication) throws ProductException, UserException;
	
	ProductResponse updateRateInProduct(RateRequest rateRequest,Long rateId, Long productId, Authentication authentication) throws RateException, ProductException, UserException;
	
	ProductResponse removeRateInProductByUser(Long rateId, Long productId, Authentication authentication) throws RateException, UserException, ProductException;
	
	List<ProductResponse> getFavourite(Authentication authentication) throws UserException;
	
}
