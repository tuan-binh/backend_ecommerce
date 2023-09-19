package ra.service.productDetail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.exception.ColorException;
import ra.exception.ProductDetailException;
import ra.exception.ProductException;
import ra.exception.SizeException;
import ra.mapper.productDetail.ProductDetailMapper;
import ra.model.domain.Product;
import ra.model.domain.ProductDetail;
import ra.model.dto.request.ProductDetailRequest;
import ra.model.dto.response.ProductDetailResponse;
import ra.repository.IProductDetailRepository;
import ra.repository.IProductRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductDetailService implements IProductDetailService {
	
	@Autowired
	private IProductDetailRepository productDetailRepository;
	@Autowired
	private IProductRepository productRepository;
	
	@Autowired
	private ProductDetailMapper productDetailMapper;
	
	@Override
	public List<ProductDetailResponse> findAllProductDetailByProduct(Long productId) throws ProductException {
		Product product = findProductById(productId);
		return productDetailRepository.findAllByProduct(product).stream()
				  .map(item -> productDetailMapper.toResponse(item))
				  .collect(Collectors.toList());
	}
	
	@Override
	public ProductDetailResponse addProductDetailToProduct(ProductDetailRequest productDetailRequest) throws ColorException, ProductException, SizeException, ProductDetailException {
		if (checkExistsProductDetail(productDetailRequest)) {
			throw new ProductDetailException("this product has in your product");
		}
		if (productDetailRequest.getStock() < 0) {
			throw new ProductDetailException("your stock must be than 0");
		}
		return productDetailMapper.toResponse(productDetailRepository.save(productDetailMapper.toEntity(productDetailRequest)));
	}
	
	@Override
	public ProductDetailResponse addStockToProductDetail(ProductDetailRequest productDetailRequest, Long productDetailId) throws ColorException, ProductException, SizeException, ProductDetailException {
		if (productDetailRequest.getStock() < 0) {
			throw new ProductDetailException("your stock must be than 0");
		}
		ProductDetail productDetail = productDetailMapper.toEntity(productDetailRequest);
		if (productDetail.getStock() == 0) {
			productDetail.setStatus(!productDetail.isStatus());
		}
		productDetail.setId(productDetailId);
		return productDetailMapper.toResponse(productDetailRepository.save(productDetail));
	}
	
	public Product findProductById(Long productId) throws ProductException {
		Optional<Product> optionalProduct = productRepository.findById(productId);
		return optionalProduct.orElseThrow(() -> new ProductException("product not found"));
	}
	
	public boolean checkExistsProductDetail(ProductDetailRequest productDetailRequest) {
		for (ProductDetail p : productDetailRepository.findAll()) {
			if (Objects.equals(p.getProduct().getId(), productDetailRequest.getProductId())) {
				if (Objects.equals(p.getColor().getId(), productDetailRequest.getColorId())) {
					if (Objects.equals(p.getSize().getId(), productDetailRequest.getSizeId())) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
}
