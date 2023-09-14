package ra.mapper.rate;

import org.springframework.stereotype.Component;
import ra.mapper.IGenericMapper;
import ra.model.domain.Rates;
import ra.model.dto.request.RateRequest;
import ra.model.dto.response.RateResponse;

@Component
public class RateMapper implements IGenericMapper<Rates, RateRequest, RateResponse> {
	
	@Override
	public Rates toEntity(RateRequest rateRequest) {
		return Rates.builder()
				  .rating(rateRequest.getRating())
				  .content(rateRequest.getContent())
				  .users(rateRequest.getUsers())
				  .product(rateRequest.getProduct())
				  .status(rateRequest.isStatus())
				  .build();
	}
	
	@Override
	public RateResponse toResponse(Rates rates) {
		return RateResponse.builder()
				  .id(rates.getId())
				  .rating(rates.getRating())
				  .content(rates.getContent())
				  .users(rates.getUsers())
				  .product(rates.getProduct())
				  .status(rates.isStatus())
				  .build();
	}
}
