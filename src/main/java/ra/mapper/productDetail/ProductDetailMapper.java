package ra.mapper.productDetail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ra.exception.ColorException;
import ra.exception.ProductException;
import ra.exception.SizeException;
import ra.mapper.IGenericMapper;
import ra.model.domain.Color;
import ra.model.domain.Product;
import ra.model.domain.ProductDetail;
import ra.model.domain.Size;
import ra.model.dto.request.ProductDetailRequest;
import ra.model.dto.response.ProductDetailResponse;
import ra.repository.IColorRepository;
import ra.repository.IProductRepository;
import ra.repository.ISizeRepository;
import ra.service.color.ColorService;
import ra.service.product.ProductService;
import ra.service.size.SizeService;

import java.util.Optional;

@Component
public class ProductDetailMapper implements IGenericMapper<ProductDetail, ProductDetailRequest, ProductDetailResponse> {
	
	@Autowired
	private IProductRepository productRepository;
	@Autowired
	private IColorRepository colorRepository;
	@Autowired
	private ISizeRepository sizeRepository;
	
	@Override
	public ProductDetail toEntity(ProductDetailRequest productDetailRequest) throws ProductException, ColorException, SizeException {
		return ProductDetail.builder()
				  .product(findProductById(productDetailRequest.getProductId()))
				  .color(findColorById(productDetailRequest.getColorId()))
				  .size(findSizeById(productDetailRequest.getSizeId()))
				  .stock(productDetailRequest.getStock())
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
				  .stock(productDetail.getStock())
				  .status(productDetail.isStatus())
				  .build();
	}
	
	public Product findProductById(Long productId) throws ProductException {
		Optional<Product> optionalProduct = productRepository.findById(productId);
		return optionalProduct.orElseThrow(() -> new ProductException("product not found"));
	}
	
	public Color findColorById(Long productId) throws ProductException {
		Optional<Color> optionalColor = colorRepository.findById(productId);
		return optionalColor.orElseThrow(() -> new ProductException("product not found"));
	}
	
	public Size findSizeById(Long productId) throws ProductException {
		Optional<Size> optionalSize = sizeRepository.findById(productId);
		return optionalSize.orElseThrow(() -> new ProductException("product not found"));
	}
	
}
