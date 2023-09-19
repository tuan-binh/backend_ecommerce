package ra.mapper.image;

import org.springframework.stereotype.Component;
import ra.exception.*;
import ra.mapper.IGenericMapper;
import ra.model.domain.ImageProduct;
import ra.model.dto.request.ImageRequest;
import ra.model.dto.response.ImageResponse;

@Component
public class ImageMapper implements IGenericMapper<ImageProduct, ImageRequest, ImageResponse> {
	
	@Override
	public ImageProduct toEntity(ImageRequest imageRequest) throws ProductException, OrderException, CouponException, CategoryException, ColorException, SizeException, ProductDetailException {
		return ImageProduct.builder()
				  // chua xay dung
				  .build();
	}
	
	@Override
	public ImageResponse toResponse(ImageProduct imageProduct) {
		return ImageResponse.builder()
				  .id(imageProduct.getId())
				  .image(imageProduct.getImage())
				  .build();
	}
}
