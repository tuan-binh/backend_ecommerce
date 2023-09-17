package ra.service.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.exception.ProductException;
import ra.exception.RoleException;
import ra.exception.UserException;
import ra.model.domain.Users;
import ra.model.dto.request.RateRequest;
import ra.model.dto.request.UserLogin;
import ra.model.dto.request.UserRegister;
import ra.model.dto.response.JwtResponse;
import ra.model.dto.response.UserResponse;

import java.util.Optional;

public interface IUserService {
	
	Page<UserResponse> findAll(Pageable pageable, Optional<String> fullName);
	
	Optional<Users> findByEmail(String email);
	
	Users save(UserRegister userRegister) throws UserException, RoleException;
	
	UserResponse findById(Long id) throws UserException;
	
	JwtResponse login(UserLogin userLogin) throws UserException;
	
	UserResponse changeStatus(Long id) throws UserException;
	
	UserResponse addProductToFavourite(Long productId, Long userId) throws UserException, ProductException;
	
	UserResponse rateProductByUser(RateRequest rateRequest, Long productId, Long userId) throws ProductException, UserException;
	
}
