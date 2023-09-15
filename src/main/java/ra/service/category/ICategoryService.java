package ra.service.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.exception.CategoryException;
import ra.model.dto.request.CategoryRequest;
import ra.model.dto.response.CategoryResponse;

import java.util.Optional;

public interface ICategoryService {
	Page<CategoryResponse> findAll(Pageable pageable, Optional<String> categoryName);
	
	CategoryResponse findById(Long id) throws CategoryException;
	
	CategoryResponse save(CategoryRequest categoryRequest);
	
	CategoryResponse update(CategoryRequest categoryRequest, Long id);
	
	CategoryResponse changeStatus(Long id) throws CategoryException;
	
}
