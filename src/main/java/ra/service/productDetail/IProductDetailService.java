package ra.service.productDetail;

import ra.exception.ColorException;
import ra.exception.ProductDetailException;
import ra.exception.ProductException;
import ra.exception.SizeException;
import ra.model.dto.request.ProductDetailRequest;
import ra.model.dto.response.ProductDetailResponse;

import java.util.List;

public interface IProductDetailService {
	
	List<ProductDetailResponse> findAllProductDetailByProduct(Long productId) throws ProductException;
	
	ProductDetailResponse addProductDetailToProduct(ProductDetailRequest productDetailRequest) throws ColorException, ProductException, SizeException, ProductDetailException;
	
	ProductDetailResponse addStockToProductDetail(ProductDetailRequest productDetailRequest, Long productDetailId) throws ColorException, ProductException, SizeException, ProductDetailException;
	
}
