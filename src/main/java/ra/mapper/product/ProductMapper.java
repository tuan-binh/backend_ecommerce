package ra.mapper.product;

import org.springframework.stereotype.Component;
import ra.mapper.IGenericMapper;
import ra.model.domain.ImageProduct;
import ra.model.domain.Product;
import ra.model.dto.request.ProductRequest;
import ra.model.dto.response.ProductResponse;

import java.util.stream.Collectors;

@Component
public class ProductMapper implements IGenericMapper<Product, ProductRequest, ProductResponse> {
	
	@Override
	public Product toEntity(ProductRequest productRequest) {
		return Product.builder()
				  .productName(productRequest.getProductName())
				  .description(productRequest.getDescription())
				  .price(productRequest.getPrice())
				  .stock(productRequest.getStock())
				  .viewCount(productRequest.getViewCount())
				  .category(productRequest.getCategory())
				  .color(productRequest.getColor())
				  .size(productRequest.getSize())
				  // nhớ thêm chuyển đổi từ multipartFile để cập nhật ảnh
				  // .images()
				  // .imageActive()
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
				  .stock(product.getStock())
				  .viewCount(product.getViewCount())
				  .category(product.getCategory())
				  .color(product.getColor())
				  .size(product.getSize())
				  .images(product.getImages().stream().map(ImageProduct::getImage).collect(Collectors.toList()))
				  .build();
	}
}
