package ra.service.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ra.exception.*;
import ra.model.dto.request.ProductDetailRequest;
import ra.model.dto.request.ProductRequest;
import ra.model.dto.request.ProductUpdate;
import ra.model.dto.response.ProductDetailResponse;
import ra.model.dto.response.ProductResponse;

import java.util.Optional;

public interface IProductService {
	
	Page<ProductResponse> findAll(Pageable pageable, Optional<String> productName);
	
	Page<ProductResponse> findAll(Pageable pageable);
	
	ProductResponse findById(Long id) throws ProductException;
	
	ProductResponse save(ProductRequest productRequest) throws ProductException, CategoryException;
	
	ProductResponse update(ProductUpdate productUpdate, Long id) throws CategoryException;
	
	ProductResponse changeStatus(Long id) throws ProductException;
	
	ProductResponse addImageToProduct(MultipartFile multipartFile, Long id) throws ProductException;
	
	ProductResponse deleteImageInProduct(Long idImage, Long idProduct) throws ImageProductException, ProductException;
	
	ProductDetailResponse addProductDetailToProduct(ProductDetailRequest productDetailRequest) throws ColorException, ProductException, SizeException;
	
	ProductResponse addCategoryToProduct(Long categoryId, Long productId) throws ProductException, CategoryException;
	
	ProductResponse removeCategoryInProduct(Long productId) throws ProductException;
	
}
