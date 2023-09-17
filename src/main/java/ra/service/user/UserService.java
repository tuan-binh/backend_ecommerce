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
import ra.exception.RoleException;
import ra.exception.UserException;
import ra.mapper.user.UserMapper;
import ra.model.domain.ERole;
import ra.model.domain.Roles;
import ra.model.domain.Users;
import ra.model.dto.request.UserLogin;
import ra.model.dto.request.UserRegister;
import ra.model.dto.response.JwtResponse;
import ra.model.dto.response.UserResponse;
import ra.repository.IUserRepository;
import ra.security.jwt.JwtEntryPoint;
import ra.security.jwt.JwtProvider;
import ra.security.user_principle.UserPrinciple;
import ra.service.role.IRoleService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
				  .favourites(userPrinciple.getFavourites())
				  .rates(userPrinciple.getRates())
				  .orders(userPrinciple.getOrders())
				  .roles(roles)
				  .status(userPrinciple.isStatus())
				  .build();
	}
}
