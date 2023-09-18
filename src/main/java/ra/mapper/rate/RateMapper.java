package ra.mapper.rate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ra.exception.ProductException;
import ra.mapper.IGenericMapper;
import ra.model.domain.Product;
import ra.model.domain.Rates;
import ra.model.dto.request.RateRequest;
import ra.model.dto.response.RateResponse;
import ra.repository.IProductRepository;
import ra.service.product.ProductService;
import ra.service.user.UserService;

import java.util.Optional;

@Component
public class RateMapper implements IGenericMapper<Rates, RateRequest, RateResponse> {
	
	@Autowired
	private IProductRepository productRepository;
	
	@Override
	public Rates toEntity(RateRequest rateRequest) throws ProductException {
		return Rates.builder()
				  .rating(rateRequest.getRating())
				  .content(rateRequest.getContent())
				  .status(rateRequest.isStatus())
				  .product(findProductById(rateRequest.getProductId()))
				  .build();
	}
	
	@Override
	public RateResponse toResponse(Rates rates) {
		return RateResponse.builder()
				  .id(rates.getId())
				  .rating(rates.getRating())
				  .content(rates.getContent())
				  .status(rates.isStatus())
				  .build();
	}
	
	public Product findProductById(Long productId) throws ProductException {
		Optional<Product> optionalProduct = productRepository.findById(productId);
		return optionalProduct.orElseThrow(() -> new ProductException("product not found"));
	}
}
