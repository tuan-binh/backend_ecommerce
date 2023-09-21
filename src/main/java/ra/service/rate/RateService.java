package ra.service.rate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ra.exception.ProductException;
import ra.exception.RateException;
import ra.exception.UserException;
import ra.mapper.rate.RateMapper;
import ra.model.domain.*;
import ra.model.dto.request.RateRequest;
import ra.model.dto.response.RateResponse;
import ra.repository.IOrderRepository;
import ra.repository.IProductRepository;
import ra.repository.IRateRepository;
import ra.repository.IUserRepository;
import ra.security.user_principle.UserPrinciple;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RateService implements IRateService {
	
	@Autowired
	private IRateRepository rateRepository;
	@Autowired
	private IUserRepository userRepository;
	@Autowired
	private IProductRepository productRepository;
	@Autowired
	private IOrderRepository orderRepository;
	@Autowired
	private RateMapper rateMapper;
	
	@Override
	public Page<RateResponse> findAll(Pageable pageable) {
		List<RateResponse> list = rateRepository.findAll(pageable).stream()
				  .map(item -> rateMapper.toResponse(item))
				  .collect(Collectors.toList());
		return new PageImpl<>(list, pageable, list.size());
	}
	
	@Override
	public Page<RateResponse> findAllByProductId(Pageable pageable, Long productId) throws ProductException {
		Product product = findProductById(productId);
		List<RateResponse> list = rateRepository.findAllByProduct(pageable, product).stream()
				  .map(item -> rateMapper.toResponse(item))
				  .collect(Collectors.toList());
		return new PageImpl<>(list, pageable, list.size());
	}
	
	@Override
	public RateResponse findById(Long id) throws RateException {
		Optional<Rates> optionalRates = rateRepository.findById(id);
		return optionalRates.map(item -> rateMapper.toResponse(item)).orElseThrow(() -> new RateException("rate not found"));
	}
	
	@Override
	public RateResponse save(RateRequest rateRequest) throws ProductException {
		return rateMapper.toResponse(rateRepository.save(rateMapper.toEntity(rateRequest)));
	}
	
	@Override
	public RateResponse update(RateRequest rateRequest, Long id) throws ProductException {
		Rates rates = rateMapper.toEntity(rateRequest);
		rates.setId(id);
		return rateMapper.toResponse(rateRepository.save(rates));
	}
	
	@Override
	public RateResponse changeStatus(Long id) throws RateException {
		Rates rates = findRatesById(id);
		return rateMapper.toResponse(rateRepository.save(rates));
	}
	
	@Override
	public RateResponse rateProductByUser(RateRequest rateRequest, Long productId, Authentication authentication) throws ProductException, UserException, RateException {
		UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
		Product product = findProductById(productId);
		Users users = findUserByUserName(userPrinciple.getEmail());
		List<Orders> orders = orderRepository.findAllByUsersIdAndStatus(userPrinciple.getId(), true);
		Rates rates = rateMapper.toEntity(rateRequest);
		boolean check = checkBooleanProduct(orders, productId);
		boolean checkRateUserInProduct = checkRateUserInProduct(product.getRates(), userPrinciple.getId());
		if (check && checkRateUserInProduct) {
			rates.setUsers(users);
			return rateMapper.toResponse(rateRepository.save(rates));
		}
		throw new RateException("You have not purchased this product yet");
	}
	
	@Override
	public RateResponse updateRateInProduct(RateRequest rateRequest, Long rateId, Authentication authentication) throws RateException, ProductException, UserException {
		UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
		Users users = findUserByUserName(userPrinciple.getEmail());
		Rates rates = rateMapper.toEntity(rateRequest);
		rates.setId(rateId);
		rates.setUsers(users);
		return rateMapper.toResponse(rateRepository.save(rates));
	}
	
	@Override
	public RateResponse removeRateInProductByUser(Long rateId, Authentication authentication) throws RateException, UserException, ProductException {
		Optional<Rates> optionalRates = rateRepository.findById(rateId);
		if (optionalRates.isPresent()) {
			rateRepository.deleteById(rateId);
			return rateMapper.toResponse(optionalRates.get());
		}
		throw new RateException("rate not found");
	}
	
	public boolean checkRateUserInProduct(List<Rates> rates, Long userId) {
		for (Rates r : rates) {
			if (Objects.equals(r.getUsers().getId(), userId)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean checkBooleanProduct(List<Orders> orders, Long productId) {
		for (Orders o : orders) {
			if (o.getEDelivered().equals(EDelivered.SUCCESS)) {
				for (CartItem c : o.getList()) {
					if (Objects.equals(c.getProductDetail().getProduct().getId(), productId)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public Rates findRatesById(Long id) throws RateException {
		Optional<Rates> optionalRates = rateRepository.findById(id);
		return optionalRates.orElseThrow(() -> new RateException("rate not found"));
	}
	
	public Product findProductById(Long productId) throws ProductException {
		Optional<Product> optionalProduct = productRepository.findById(productId);
		return optionalProduct.orElseThrow(() -> new ProductException("product not found"));
	}
	
	public Users findUserByUserName(String email) throws UserException {
		Optional<Users> optionalUsers = userRepository.findByEmail(email);
		return optionalUsers.orElseThrow(() -> new UserException("user not found"));
	}
	
}
