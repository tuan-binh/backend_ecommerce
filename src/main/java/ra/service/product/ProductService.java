package ra.service.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ra.exception.ProductException;
import ra.mapper.product.ProductMapper;
import ra.model.domain.ImageProduct;
import ra.model.domain.Product;
import ra.model.dto.request.ProductRequest;
import ra.model.dto.request.ProductUpdate;
import ra.model.dto.response.ProductResponse;
import ra.repository.IProductRepository;
import ra.service.upload_aws.StorageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {
	
	@Autowired
	private IProductRepository productRepository;
	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private StorageService storageService;
	
	@Override
	public Page<ProductResponse> findAll(Pageable pageable, String productName) {
		List<ProductResponse> list;
		if (!productName.isEmpty()) {
			list = productRepository.findAllByProductNameContaining(pageable, productName).stream()
					  .map(item -> productMapper.toResponse(item))
					  .collect(Collectors.toList());
		} else {
			list = productRepository.findAll(pageable).stream()
					  .map(item -> productMapper.toResponse(item))
					  .collect(Collectors.toList());
		}
		return new PageImpl<>(list, pageable, list.size());
	}
	
	@Override
	public ProductResponse findById(Long id) throws ProductException {
		Optional<Product> optionalProduct = productRepository.findById(id);
		return optionalProduct.map(item -> productMapper.toResponse(item)).orElseThrow(() -> new ProductException("product not found"));
	}
	
	@Override
	public ProductResponse save(ProductRequest productRequest) {
		Product product = productMapper.toEntity(productRequest);
		List<String> listUrl = new ArrayList<>();
		for (MultipartFile m : productRequest.getFile()) {
			listUrl.add(storageService.uploadFile(m));
		}
		// set image active vào cái ảnh đầu tiên
		product.setImageActive(listUrl.get(0));
		List<ImageProduct> images = new ArrayList<>();
		for (String url : listUrl) {
			images.add(ImageProduct.builder().image(url).product(product).build());
		}
		// set list image vào product
		product.setImages(images);
		return productMapper.toResponse(productRepository.save(product));
	}
	
	@Override
	public ProductResponse update(ProductUpdate productUpdate, Long id) {
		Product product = productMapper.toEntity(productUpdate);
		product.setId(id);
		return productMapper.toResponse(productRepository.save(product));
	}
	
	@Override
	public ProductResponse changeStatus(Long id) throws ProductException {
		Product product = findProductById(id);
		product.setStatus(!product.isStatus());
		return productMapper.toResponse(productRepository.save(product));
	}
	
	public Product findProductById(Long id) throws ProductException {
		Optional<Product> optionalProduct = productRepository.findById(id);
		return optionalProduct.orElseThrow(() -> new ProductException("product not found"));
	}
	
	@Override
	public ProductResponse addImageToProduct(MultipartFile multipartFile, Long id) throws ProductException {
		Product product = findProductById(id);
		String url = storageService.uploadFile(multipartFile);
		product.getImages().add(ImageProduct.builder().image(url).product(product).build());
		return productMapper.toResponse(productRepository.save(product));
	}
	
	
}
