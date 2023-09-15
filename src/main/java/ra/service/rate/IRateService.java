package ra.service.rate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.exception.RateException;
import ra.model.dto.request.RateRequest;
import ra.model.dto.response.RateResponse;

public interface IRateService {
	Page<RateResponse> findAll(Pageable pageable);
	
	RateResponse findById(Long id) throws RateException;
	
	RateResponse save(RateRequest rateRequest);
	
	RateResponse update(RateRequest rateRequest, Long id);
	
	RateResponse changeStatus(Long id) throws RateException;
	
}
