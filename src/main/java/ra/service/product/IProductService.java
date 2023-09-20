package ra.service.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.exception.*;
import ra.model.dto.request.ImageRequest;
import ra.model.dto.request.ProductRequest;
import ra.model.dto.request.ProductUpdate;
import ra.model.dto.response.ImageResponse;
import ra.model.dto.response.ProductResponse;

import java.util.List;
import java.util.Optional;

public interface IProductService {
	
	Page<ProductResponse> findAll(Pageable pageable, Optional<String> productName);
	
	Page<ProductResponse> findAll(Pageable pageable);
	
	ProductResponse findById(Long id) throws ProductException;
	
	ProductResponse save(ProductRequest productRequest) throws ProductException, CategoryException, ImageProductException;
	
	ProductResponse update(ProductUpdate productUpdate, Long id) throws CategoryException;
	
	ProductResponse changeStatus(Long id) throws ProductException;
	
	ImageResponse changeImageAvatar(Long imageId, Long productId) throws ImageProductException, ProductException;
	
	List<ImageResponse> addImageToProduct(ImageRequest imageRequest) throws ProductException, ColorException, CouponException, CategoryException, ProductDetailException, OrderException, SizeException, ImageProductException;
	
	ImageResponse deleteImageInProduct(Long idImage) throws ImageProductException, ProductException;
	
	ProductResponse addCategoryToProduct(Long categoryId, Long productId) throws ProductException, CategoryException;
	
	ProductResponse removeCategoryInProduct(Long productId) throws ProductException;
	
}
