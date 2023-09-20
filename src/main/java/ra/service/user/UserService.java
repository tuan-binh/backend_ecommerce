package ra.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.exception.OrderException;
import ra.exception.RoleException;
import ra.exception.UserException;
import ra.mapper.user.UserMapper;
import ra.model.domain.*;
import ra.model.dto.request.ChangePassword;
import ra.model.dto.request.UserLogin;
import ra.model.dto.request.UserRegister;
import ra.model.dto.request.UserUpdate;
import ra.model.dto.response.CountOrderByUser;
import ra.model.dto.response.JwtResponse;
import ra.model.dto.response.RevenueByMonth;
import ra.model.dto.response.UserResponse;
import ra.repository.IOrderRepository;
import ra.repository.IUserRepository;
import ra.security.jwt.JwtEntryPoint;
import ra.security.jwt.JwtProvider;
import ra.security.user_principle.UserPrinciple;
import ra.service.role.IRoleService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
	
	public final Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);
	
	@Autowired
	private IUserRepository userRepository;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private IOrderRepository orderRepository;
	@Autowired
	private JwtProvider jwtProvider;
	
	@Override
	public Page<UserResponse> findAll(Pageable pageable, Optional<String> fullName) {
		List<UserResponse> list = fullName.map(s -> userRepository.findAllByFullNameContaining(pageable, s).stream()
				  .map(user -> userMapper.toResponse(user))
				  .collect(Collectors.toList())).orElseGet(() -> userRepository.findAll(pageable).stream()
				  .map(user -> userMapper.toResponse(user))
				  .collect(Collectors.toList()));
		return new PageImpl<>(list, pageable, list.size());
	}
	
	@Override
	public Optional<Users> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	@Override
	public Users save(UserRegister userRegister) throws UserException, RoleException {
		if (userRepository.existsByEmail(userRegister.getEmail())) {
			logger.error("exists email");
			throw new UserException("Email is exists");
		}
		
		Set<Roles> roles = new HashSet<>();
		if (userRegister.getRoles() == null || userRegister.getRoles().isEmpty()) {
			roles.add(roleService.findByRoleName(ERole.ROLE_USER));
		} else {
			userRegister.getRoles().forEach(role -> {
				switch (role) {
					case "admin":
						try {
							roles.add(roleService.findByRoleName(ERole.ROLE_ADMIN));
						} catch (RoleException e) {
							throw new RuntimeException(e);
						}
					case "user":
						try {
							roles.add(roleService.findByRoleName(ERole.ROLE_USER));
						} catch (RoleException e) {
							throw new RuntimeException(e);
						}
						break;
					default:
						try {
							throw new RoleException("don't have role " + role);
						} catch (RoleException e) {
							throw new RuntimeException(e);
						}
				}
			});
		}
		return userRepository.save(Users.builder()
				  .fullName(userRegister.getFullName())
				  .email(userRegister.getEmail())
				  .password(passwordEncoder.encode(userRegister.getPassword()))
				  .roles(roles)
				  .status(true)
				  .build());
	}
	
	@Override
	public UserResponse findById(Long id) throws UserException {
		Optional<Users> users = userRepository.findById(id);
		logger.error("find users");
		return users.map(item -> userMapper.toResponse(item)).orElseThrow(() -> new UserException("not found user"));
	}
	
	@Override
	public JwtResponse login(UserLogin userLogin) throws UserException {
		Authentication authentication = null;
		try {
			authentication = authenticationManager.authenticate(
					  new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword())
			);
		} catch (AuthenticationException e) {
			throw new UserException("Username or Password is incorrect");
		}
		
		UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
		if (!userPrinciple.isStatus()) {
			throw new UserException("your account is blocked");
		}
		String token = jwtProvider.generateToken(userPrinciple);
		List<String> roles = userPrinciple.getAuthorities().stream()
				  .map(GrantedAuthority::getAuthority)
				  .collect(Collectors.toList());
		
		return JwtResponse.builder()
				  .token(token)
				  .fullName(userPrinciple.getFullName())
				  .email(userPrinciple.getEmail())
				  .phone(userPrinciple.getPhone())
				  .address(userPrinciple.getAddress())
				  .roles(roles)
				  .status(userPrinciple.isStatus())
				  .build();
	}
	
	@Override
	public UserResponse changeStatus(Long id) throws UserException {
		Users users = findUserById(id);
		users.setStatus(!users.isStatus());
		return userMapper.toResponse(userRepository.save(users));
	}
	
	@Override
	public UserResponse updateYourInfo(UserUpdate userUpdate, Authentication authentication) throws UserException {
		if (userUpdate.getPhone().trim().isEmpty() || userUpdate.getAddress().trim().isEmpty()) {
			throw new UserException("you must be update full your information");
		}
		Users users = findUserByAuthentication(authentication);
		users.setPhone(userUpdate.getPhone());
		users.setAddress(userUpdate.getAddress());
		return userMapper.toResponse(userRepository.save(users));
	}
	
	@Override
	public UserResponse changePassword(ChangePassword changePassword, Authentication authentication) throws UserException {
		if (changePassword.getPassword().trim().isEmpty()) {
			throw new UserException("you does not blank");
		}
		Users users = findUserByAuthentication(authentication);
		users.setPassword(passwordEncoder.encode(changePassword.getPassword()));
		return userMapper.toResponse(userRepository.save(users));
	}
	
	@Override
	public List<CountOrderByUser> handleStatistical() throws OrderException, UserException {
		List<CountOrderByUser> list = new ArrayList<>();
		for (Long id : getAllIdUser()) {
			Users users = findUserById(id);
			if (users.getRoles().stream().noneMatch(item -> item.getRoleName().equals(ERole.ROLE_ADMIN))) {
				list.add(CountOrderByUser.builder()
						  .fullName(users.getFullName())
						  .email(users.getEmail())
						  .quantity(orderRepository.countOrdersByUsersIdAndStatus(id, true))
						  .build());
			}
		}
		list.sort(Comparator.comparingInt(CountOrderByUser::getQuantity).reversed());
		return list;
	}
	
	@Override
	public List<RevenueByMonth> getStatisticalRevenue(String year) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		List<RevenueByMonth> list = new ArrayList<>();
		for (int i = 0; i < 12; i++) {
			List<Orders> orderByMonth = new ArrayList<>();
			for (Orders o : orderRepository.findAll()) {
				LocalDate date = LocalDate.parse(o.getDeliveryTime().toString(), formatter);
				if (o.isStatus() && o.getEDelivered().equals(EDelivered.SUCCESS) && date.getMonthValue() == (i + 1) && date.getYear() == Integer.parseInt(year)) {
					orderByMonth.add(o);
				}
			}
			double sum = 0;
			for (Orders o : orderByMonth) {
				sum += o.getTotal();
			}
			list.add(RevenueByMonth.builder()
					  .month(i + 1)
					  .quantity(orderByMonth.size())
					  .revenue(sum)
					  .build());
		}
		
		return list;
	}
	
	public List<Long> getAllIdUser() {
		List<Long> list = new ArrayList<>();
		userRepository.findAll().forEach(item -> list.add(item.getId()));
		return list;
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
	
	public Users findUserById(Long id) throws UserException {
		Optional<Users> optionalUsers = userRepository.findById(id);
		return optionalUsers.orElseThrow(() -> new UserException("user not found"));
	}
	
}
