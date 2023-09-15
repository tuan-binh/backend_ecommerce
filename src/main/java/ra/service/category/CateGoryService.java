package ra.service.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.exception.CategoryException;
import ra.mapper.category.CategoryMapper;
import ra.model.domain.Category;
import ra.model.dto.request.CategoryRequest;
import ra.model.dto.response.CategoryResponse;
import ra.repository.ICategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CateGoryService implements ICategoryService {
	
	@Autowired
	private ICategoryRepository categoryRepository;
	@Autowired
	private CategoryMapper categoryMapper;
	
	@Override
	public Page<CategoryResponse> findAll(Pageable pageable, Optional<String> categoryName) {
		List<CategoryResponse> list = categoryName.map(s -> categoryRepository.findAllByCategoryNameContaining(pageable, s).stream()
				  .map(item -> categoryMapper.toResponse(item))
				  .collect(Collectors.toList())).orElseGet(() -> categoryRepository.findAll(pageable).stream()
				  .map(item -> categoryMapper.toResponse(item))
				  .collect(Collectors.toList()));
		return new PageImpl<>(list, pageable, list.size());
	}
	
	@Override
	public CategoryResponse findById(Long id) throws CategoryException {
		Optional<Category> optionalCategory = categoryRepository.findById(id);
		return optionalCategory.map(item -> categoryMapper.toResponse(item)).orElseThrow(() -> new CategoryException("not found category"));
	}
	
	@Override
	public CategoryResponse save(CategoryRequest categoryRequest) {
		return categoryMapper.toResponse(categoryRepository.save(categoryMapper.toEntity(categoryRequest)));
	}
	
	@Override
	public CategoryResponse update(CategoryRequest categoryRequest, Long id) {
		Category category = categoryMapper.toEntity(categoryRequest);
		category.setId(id);
		return categoryMapper.toResponse(categoryRepository.save(category));
	}
	
	@Override
	public CategoryResponse changeStatus(Long id) throws CategoryException {
		Category category = findCategoryById(id);
		category.setStatus(!category.isStatus());
		return categoryMapper.toResponse(categoryRepository.save(category));
	}
	
	public Category findCategoryById(Long id) throws CategoryException {
		Optional<Category> optionalCategory = categoryRepository.findById(id);
		return optionalCategory.orElseThrow(() -> new CategoryException("not found category"));
	}
	
}
