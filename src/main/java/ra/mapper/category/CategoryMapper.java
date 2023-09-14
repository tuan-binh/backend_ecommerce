package ra.mapper.category;

import org.springframework.stereotype.Component;
import ra.mapper.IGenericMapper;
import ra.model.domain.Category;
import ra.model.dto.request.CategoryRequest;
import ra.model.dto.response.CategoryResponse;

@Component
public class CategoryMapper implements IGenericMapper<Category, CategoryRequest, CategoryResponse> {
	
	@Override
	public Category toEntity(CategoryRequest categoryRequest) {
		return Category.builder()
				  .categoryName(categoryRequest.getCategoryName())
				  .status(categoryRequest.isStatus())
				  .build();
	}
	
	@Override
	public CategoryResponse toResponse(Category category) {
		return CategoryResponse.builder()
				  .id(category.getId())
				  .categoryName(category.getCategoryName())
				  .status(category.isStatus())
				  .build();
	}
}
