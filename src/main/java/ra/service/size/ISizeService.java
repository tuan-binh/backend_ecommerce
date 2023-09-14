package ra.service.size;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.model.dto.request.SizeRequest;
import ra.model.dto.response.SizeResponse;

import java.util.Optional;

public interface ISizeService {
	Page<SizeResponse> findAll(Pageable pageable, Optional<String> sizeName);
	
	SizeResponse findById(Long id);
	
	SizeResponse save(SizeRequest sizeRequest);
	
	SizeResponse update(SizeRequest sizeRequest, Long id);
	
	SizeResponse changeStatus(Long id);
}
