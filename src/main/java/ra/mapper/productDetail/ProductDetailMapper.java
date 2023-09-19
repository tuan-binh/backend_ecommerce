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
				  .product(productDetail.getProduct().getProductName())
				  .color(productDetail.getColor().getColorName())
				  .size(productDetail.getSize().getSizeName())
				  .stock(productDetail.getStock())
				  .status(productDetail.isStatus())
				  .build();
	}
	
	public Product findProductById(Long productId) throws ProductException {
		Optional<Product> optionalProduct = productRepository.findById(productId);
		return optionalProduct.orElseThrow(() -> new ProductException("product not found"));
	}
	
	public Color findColorById(Long colorId) throws ColorException {
		Optional<Color> optionalColor = colorRepository.findById(colorId);
		return optionalColor.orElseThrow(() -> new ColorException("color not found"));
	}
	
	public Size findSizeById(Long sizeId) throws SizeException {
		Optional<Size> optionalSize = sizeRepository.findById(sizeId);
		return optionalSize.orElseThrow(() -> new SizeException("size not found"));
	}
	
}
