package ra.service.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import ra.exception.OrderException;
import ra.exception.RoleException;
import ra.exception.UserException;
import ra.model.domain.Users;
import ra.model.dto.request.ChangePassword;
import ra.model.dto.request.UserLogin;
import ra.model.dto.request.UserRegister;
import ra.model.dto.request.UserUpdate;
import ra.model.dto.response.CountOrderByUser;
import ra.model.dto.response.JwtResponse;
import ra.model.dto.response.RevenueByMonth;
import ra.model.dto.response.UserResponse;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

public interface IUserService {
	
	Page<UserResponse> findAll(Pageable pageable, Optional<String> fullName);
	
	Optional<Users> findByEmail(String email);
	
	Users save(UserRegister userRegister) throws UserException, RoleException;
	
	UserResponse findById(Long id) throws UserException;
	
	JwtResponse login(HttpSession session, UserLogin userLogin) throws UserException;
	
	UserResponse changeStatus(Long id) throws UserException;
	
	UserResponse updateYourInfo(UserUpdate userUpdate, Authentication authentication) throws UserException;
	
	UserResponse changePassword(ChangePassword changePassword, Authentication authentication) throws UserException;
	
	List<CountOrderByUser> handleStatistical() throws OrderException, UserException;
	
	List<RevenueByMonth> getStatisticalRevenue(String year);
	
	void getNewPasswordWithEmail(String email) throws UserException, MessagingException;
	
}
