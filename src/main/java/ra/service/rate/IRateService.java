package ra.service.rate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import ra.exception.ProductException;
import ra.exception.RateException;
import ra.exception.UserException;
import ra.model.dto.request.RateRequest;
import ra.model.dto.response.ProductResponse;
import ra.model.dto.response.RateResponse;

public interface IRateService {
	Page<RateResponse> findAll(Pageable pageable);
	
	Page<RateResponse> findAllByProductId(Pageable pageable, Long productId) throws ProductException;
	
	RateResponse findById(Long id) throws RateException;
	
	RateResponse save(RateRequest rateRequest);
	
	RateResponse update(RateRequest rateRequest, Long id);
	
	RateResponse changeStatus(Long id) throws RateException;
	
	RateResponse rateProductByUser(RateRequest rateRequest, Long productId, Authentication authentication) throws ProductException, UserException;
	
	RateResponse updateRateInProduct(RateRequest rateRequest,Long rateId, Long productId, Authentication authentication) throws RateException, ProductException, UserException;
	
	RateResponse removeRateInProductByUser(Long rateId, Long productId, Authentication authentication) throws RateException, UserException, ProductException;
	
}
