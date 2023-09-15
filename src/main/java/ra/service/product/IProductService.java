package ra.service.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ra.exception.ProductException;
import ra.model.dto.request.ProductRequest;
import ra.model.dto.request.ProductUpdate;
import ra.model.dto.response.ProductResponse;

import java.util.Optional;

public interface IProductService {
	
	Page<ProductResponse> findAll(Pageable pageable, String productName);
	
	ProductResponse findById(Long id) throws ProductException;
	
	ProductResponse save(ProductRequest productRequest);
	
	ProductResponse update(ProductUpdate productUpdate, Long id);
	
	ProductResponse changeStatus(Long id) throws ProductException;
	
	ProductResponse addImageToProduct(MultipartFile multipartFile, Long id) throws ProductException;
	
}
