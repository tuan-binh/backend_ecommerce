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
import ra.exception.ProductException;
import ra.exception.RateException;
import ra.exception.RoleException;
import ra.exception.UserException;
import ra.mapper.product.ProductMapper;
import ra.mapper.rate.RateMapper;
import ra.mapper.user.UserMapper;
import ra.model.domain.*;
import ra.model.dto.request.RateRequest;
import ra.model.dto.request.UserLogin;
import ra.model.dto.request.UserRegister;
import ra.model.dto.response.JwtResponse;
import ra.model.dto.response.ProductResponse;
import ra.model.dto.response.UserResponse;
import ra.repository.IProductRepository;
import ra.repository.IRateRepository;
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
	@Autowired
	private IProductRepository productRepository;
	@Autowired
	private IRateRepository rateRepository;
	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private RateMapper rateMapper;
	
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
	public List<ProductResponse> addProductToFavourite(Long productId, Authentication authentication) throws UserException, ProductException {
		Users users = findUserByAuthentication(authentication);
		Product product = findProductById(productId);
		if (users.getFavourites().contains(product)) {
			throw new UserException("You has product in your favourite");
		}
		users.getFavourites().add(product);
		return userRepository.save(users).getFavourites().stream()
				  .map(item -> productMapper.toResponse(item))
				  .collect(Collectors.toList());
	}
	
	@Override
	public List<ProductResponse> removeProductInFavourite(Long productId, Authentication authentication) throws UserException, ProductException {
		Users users = findUserByAuthentication(authentication);
		Product product = findProductById(productId);
		users.getFavourites().remove(product);
		return userRepository.save(users).getFavourites().stream()
				  .map(item -> productMapper.toResponse(item))
				  .collect(Collectors.toList());
	}
	
//	@Override
//	public ProductResponse rateProductByUser(RateRequest rateRequest, Long productId, Authentication authentication) throws ProductException, UserException {
//		Product product = findProductById(productId);
//		Users users = findUserByAuthentication(authentication);
//		Rates rate = Rates.builder()
//				  .rating(rateRequest.getRating())
//				  .content(rateRequest.getContent())
//				  .product(product)
//				  .users(users)
//				  .status(true)
//				  .build();
//		product.getRates().add(rate);
//		users.getRates().add(rate);
//		userRepository.save(users);
//		return productMapper.toResponse(productRepository.save(product));
//	}
//
//	@Override
//	public ProductResponse updateRateInProduct(RateRequest rateRequest, Long rateId, Long productId, Authentication authentication) throws RateException, ProductException, UserException {
//		Users users = findUserByAuthentication(authentication);
//		Product product = findProductById(productId);
//		Rates rate = rateMapper.toEntity(rateRequest);
//		rate.setId(rateId);
//		rateRepository.save(rate);
//		product.getRates().set(product.getRates().indexOf(findRateById(rateId)), rate);
//		users.getRates().set(users.getRates().indexOf(findRateById(rateId)), rate);
//		userRepository.save(users);
//		return productMapper.toResponse(productRepository.save(product));
//	}
//
//	@Override
//	public ProductResponse removeRateInProductByUser(Long rateId, Long productId, Authentication authentication) throws RateException, UserException, ProductException {
//		Users users = findUserByAuthentication(authentication);
//		Rates rates = findRateById(rateId);
//		Product product = findProductById(productId);
//		product.getRates().remove(rates);
//		users.getRates().remove(rates);
//		userRepository.save(users);
//		rateRepository.deleteById(rateId);
//		return productMapper.toResponse(productRepository.save(product));
//
//	}
	
	public Rates findRateById(Long rateId) throws RateException {
		Optional<Rates> optionalRates = rateRepository.findById(rateId);
		return optionalRates.orElseThrow(() -> new RateException("rate not found"));
	}
	
	public Product findProductById(Long productId) throws ProductException {
		Optional<Product> optionalProduct = productRepository.findById(productId);
		return optionalProduct.orElseThrow(() -> new ProductException("product not found"));
	}
	
	public Users findUserById(Long id) throws UserException {
		Optional<Users> optionalUsers = userRepository.findById(id);
		return optionalUsers.orElseThrow(() -> new UserException("user not found"));
	}
	
	@Override
	public List<ProductResponse> getFavourite(Authentication authentication) throws UserException {
		return findUserByAuthentication(authentication).getFavourites().stream()
				  .map(item -> productMapper.toResponse(item))
				  .collect(Collectors.toList());
	}
	
	public Users findUserByAuthentication(Authentication authentication) throws UserException {
		if (authentication != null && authentication.isAuthenticated()) {
			String username = authentication.getName();
			return findUserByEmail(username);
		}
		throw new UserException("Un Authentication");
	}
	
	public Users findUserByEmail(String email) throws UserException {
		Optional<Users> optionalUsers = findByEmail(email);
		return optionalUsers.orElseThrow(() -> new UserException("user not found"));
	}
	
}
