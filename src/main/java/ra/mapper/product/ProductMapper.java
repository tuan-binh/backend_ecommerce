package ra.mapper.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ra.exception.CategoryException;
import ra.mapper.IGenericMapper;
import ra.model.domain.Category;
import ra.model.domain.Product;
import ra.model.dto.request.ProductRequest;
import ra.model.dto.request.ProductUpdate;
import ra.model.dto.response.ProductResponse;
import ra.repository.ICategoryRepository;

import java.util.Optional;

@Component
public class ProductMapper implements IGenericMapper<Product, ProductRequest, ProductResponse> {
	
	@Autowired
	private ICategoryRepository categoryRepository;
	
	@Override
	public Product toEntity(ProductRequest productRequest) throws CategoryException {
		return Product.builder()
				  .productName(productRequest.getProductName())
				  .description(productRequest.getDescription())
				  .price(productRequest.getPrice())
				  .countBuy(productRequest.getCountBuy())
				  .category(findCategoryById(productRequest.getCategoryId()))
				  // nhớ thêm chuyển đổi từ multipartFile để cập nhật ảnh
				  // .images()
				  // .imageActive()
				  .status(productRequest.isStatus())
				  .build();
	}
	
	public Product toEntity(ProductUpdate productUpdate) throws CategoryException {
		return Product.builder()
				  .productName(productUpdate.getProductName())
				  .description(productUpdate.getDescription())
				  .price(productUpdate.getPrice())
				  .category(findCategoryById(productUpdate.getCategoryId()))
				  .status(productUpdate.isStatus())
				  .build();
	}
	
	@Override
	public ProductResponse toResponse(Product product) {
		return ProductResponse.builder()
				  .id(product.getId())
				  .productName(product.getProductName())
				  .imageActive(product.getImageActive())
				  .description(product.getDescription())
				  .price(product.getPrice())
				  .category(product.getCategory())
				  .status(product.isStatus())
				  .build();
	}
	
	public Category findCategoryById(Long categoryId) throws CategoryException {
		Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
		return optionalCategory.orElseThrow(() -> new CategoryException("category not found"));
	}
}
