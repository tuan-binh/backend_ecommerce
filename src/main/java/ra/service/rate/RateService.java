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
import ra.model.domain.Product;
import ra.model.domain.Rates;
import ra.model.domain.Users;
import ra.model.dto.request.RateRequest;
import ra.model.dto.response.RateResponse;
import ra.repository.IProductRepository;
import ra.repository.IRateRepository;
import ra.repository.IUserRepository;

import java.util.List;
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
		rates.setStatus(!rates.isStatus());
		return rateMapper.toResponse(rateRepository.save(rates));
	}
	
	@Override
	public RateResponse rateProductByUser(RateRequest rateRequest, Authentication authentication) throws ProductException, UserException {
		Users users = findUserByAuthentication(authentication);
		Rates rates = rateMapper.toEntity(rateRequest);
		rates.setUsers(users);
		return rateMapper.toResponse(rateRepository.save(rates));
	}
	
	@Override
	public RateResponse updateRateInProduct(RateRequest rateRequest, Long rateId, Authentication authentication) throws RateException, ProductException, UserException {
		Users users = findUserByAuthentication(authentication);
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
	
	public Rates findRatesById(Long id) throws RateException {
		Optional<Rates> optionalRates = rateRepository.findById(id);
		return optionalRates.orElseThrow(() -> new RateException("rate not found"));
	}
	
	public Product findProductById(Long productId) throws ProductException {
		Optional<Product> optionalProduct = productRepository.findById(productId);
		return optionalProduct.orElseThrow(() -> new ProductException("product not found"));
	}
	
	public Users findUserByAuthentication(Authentication authentication) throws UserException {
		if (authentication != null && authentication.isAuthenticated()) {
			String username = authentication.getName();
			return findUserByEmail(username);
		}
		throw new UserException("Un Authentication");
	}
	
	public Users findUserByEmail(String email) throws UserException {
		Optional<Users> optionalUsers = userRepository.findByEmail(email);
		return optionalUsers.orElseThrow(() -> new UserException("user not found"));
	}
	
}
