package ra.mapper.product;

import org.springframework.stereotype.Component;
import ra.mapper.IGenericMapper;
import ra.model.domain.Product;
import ra.model.dto.request.ProductRequest;
import ra.model.dto.request.ProductUpdate;
import ra.model.dto.response.ProductResponse;

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
				  .status(productRequest.isStatus())
				  .build();
	}
	
	public Product toEntity(ProductUpdate productUpdate) {
		return Product.builder()
				  .productName(productUpdate.getProductName())
				  .description(productUpdate.getDescription())
				  .price(productUpdate.getPrice())
				  .stock(productUpdate.getStock())
				  .viewCount(productUpdate.getViewCount())
				  .category(productUpdate.getCategory())
				  .color(productUpdate.getColor())
				  .size(productUpdate.getSize())
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
				  .stock(product.getStock())
				  .viewCount(product.getViewCount())
				  .category(product.getCategory())
				  .color(product.getColor())
				  .size(product.getSize())
				  .images(product.getImages())
				  .rates(product.getRates())
				  .status(product.isStatus())
				  .build();
	}
}
