package ra.mapper.productDetail;

import org.springframework.stereotype.Component;
import ra.mapper.IGenericMapper;
import ra.model.domain.ProductDetail;
import ra.model.dto.request.ProductDetailRequest;
import ra.model.dto.response.ProductDetailResponse;

@Component
public class ProductDetailMapper implements IGenericMapper<ProductDetail, ProductDetailRequest, ProductDetailResponse> {
	
	@Override
	public ProductDetail toEntity(ProductDetailRequest productDetailRequest) {
		return ProductDetail.builder()
				  .product(productDetailRequest.getProduct())
				  .color(productDetailRequest.getColor())
				  .size(productDetailRequest.getSize())
				  .status(productDetailRequest.isStatus())
				  .build();
	}
	
	@Override
	public ProductDetailResponse toResponse(ProductDetail productDetail) {
		return ProductDetailResponse.builder()
				  .id(productDetail.getId())
				  .product(productDetail.getProduct())
				  .color(productDetail.getColor())
				  .size(productDetail.getSize())
				  .status(productDetail.isStatus())
				  .build();
	}
}
