package ra.service.color;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.exception.ColorException;
import ra.model.dto.request.ColorRequest;
import ra.model.dto.response.ColorResponse;

import java.util.Optional;

public interface IColorService {
	
	Page<ColorResponse> findAll(Pageable pageable, Optional<String> colorName);
	
	ColorResponse findById(Long id) throws ColorException;
	
	ColorResponse save(ColorRequest colorRequest) throws ColorException;
	
	ColorResponse update(ColorRequest colorRequest, Long id);
	
	ColorResponse changeStatus(Long id) throws ColorException;
	
}
