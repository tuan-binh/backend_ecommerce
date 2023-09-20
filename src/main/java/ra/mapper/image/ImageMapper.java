package ra.mapper.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ra.exception.*;
import ra.mapper.IGenericMapper;
import ra.model.domain.ImageProduct;
import ra.model.domain.Product;
import ra.model.dto.request.ImageRequest;
import ra.model.dto.response.ImageResponse;
import ra.repository.IProductRepository;

import java.util.Optional;

@Component
public class ImageMapper implements IGenericMapper<ImageProduct, ImageRequest, ImageResponse> {
	
	@Autowired
	private IProductRepository productRepository;
	
	@Override
	public ImageProduct toEntity(ImageRequest imageRequest) throws ProductException, OrderException, CouponException, CategoryException, ColorException, SizeException, ProductDetailException {
		return ImageProduct.builder()
				  .product(findProductById(imageRequest.getProductId()))
				  // upload image
				  .build();
	}
	
	@Override
	public ImageResponse toResponse(ImageProduct imageProduct) {
		return ImageResponse.builder()
				  .id(imageProduct.getId())
				  .image(imageProduct.getImage())
				  .build();
	}
	
	public Product findProductById(Long productId) throws ProductException {
		Optional<Product> optionalProduct = productRepository.findById(productId);
		return optionalProduct.orElseThrow(() -> new ProductException("product not found"));
	}
	
}
