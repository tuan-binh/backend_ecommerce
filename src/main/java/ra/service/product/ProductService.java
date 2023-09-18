package ra.service.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ra.exception.*;
import ra.mapper.product.ProductMapper;
import ra.model.domain.*;
import ra.model.dto.request.ProductRequest;
import ra.model.dto.request.ProductUpdate;
import ra.model.dto.response.ProductResponse;
import ra.repository.*;
import ra.service.upload_aws.StorageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {
	
	@Autowired
	private IProductRepository productRepository;
	@Autowired
	private IImageProductRepository iImageProductRepository;
	@Autowired
	private IColorRepository colorRepository;
	@Autowired
	private ISizeRepository sizeRepository;
	@Autowired
	private ICategoryRepository categoryRepository;
	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private StorageService storageService;
	
	@Override
	public Page<ProductResponse> findAll(Pageable pageable, Optional<String> productName) {
		List<ProductResponse> list = productName.map(s -> productRepository.findAllByProductNameContaining(pageable, s).stream()
				  .map(item -> productMapper.toResponse(item))
				  .collect(Collectors.toList())).orElseGet(() -> productRepository.findAll(pageable).stream()
				  .map(item -> productMapper.toResponse(item))
				  .collect(Collectors.toList()));
		return new PageImpl<>(list, pageable, list.size());
	}
	
	@Override
	public Page<ProductResponse> findAll(Pageable pageable) {
		List<ProductResponse> list = productRepository.findAll(pageable).stream()
				  .map(item -> productMapper.toResponse(item))
				  .collect(Collectors.toList());
		return new PageImpl<>(list, pageable, list.size());
	}
	
	@Override
	public ProductResponse findById(Long id) throws ProductException {
		Optional<Product> optionalProduct = productRepository.findById(id);
		if (optionalProduct.isPresent()) {
			return productMapper.toResponse(optionalProduct.get());
		}
		throw new ProductException("product not found");
//		return optionalProduct.map(item -> productMapper.toResponse(item)).orElseThrow(() -> new ProductException("product not found"));
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
	
	@Override
	public ProductResponse deleteImageInProduct(Long idImage, Long idProduct) throws ImageProductException, ProductException {
		ImageProduct imageProduct = findImageProductById(idImage);
		Product product = findProductById(idProduct);
		product.getImages().add(imageProduct);
		return productMapper.toResponse(productRepository.save(product));
	}
	
	@Override
	public ProductResponse deleteColorInProduct(Long productId, Long colorId) throws ProductException, ColorException {
		Product product = findProductById(productId);
		Color color = findColorById(colorId);
		product.getColor().remove(color);
		return productMapper.toResponse(productRepository.save(product));
	}
	
	@Override
	public ProductResponse deleteSizeInProduct(Long productId, Long sizeId) throws ProductException, SizeException {
		Product product = findProductById(productId);
		Size size = findSizeById(sizeId);
		product.getSize().remove(size);
		return productMapper.toResponse(productRepository.save(product));
	}
	
	@Override
	public ProductResponse addCategoryToProduct(Long categoryId, Long productId) throws ProductException, CategoryException {
		Product product = findProductById(productId);
		Category category = findCategoryById(categoryId);
		product.setCategory(category);
		return productMapper.toResponse(productRepository.save(product));
	}
	
	@Override
	public ProductResponse removeCategoryInProduct(Long productId) throws ProductException {
		Product product = findProductById(productId);
		product.setCategory(null);
		return productMapper.toResponse(productRepository.save(product));
	}
	
	@Override
	public ProductResponse addColorToProduct(Long colorId, Long productId) throws ColorException, ProductException {
		Color color = findColorById(colorId);
		Product product = findProductById(productId);
		boolean check = product.getColor().contains(color);
		if(check) {
			throw new ProductException("product has this color");
		}
		product.getColor().add(color);
		return productMapper.toResponse(productRepository.save(product));
	}
	
	@Override
	public ProductResponse addSizeToProduct(Long sizeId, Long productId) throws SizeException, ProductException {
		Size size = findSizeById(sizeId);
		Product product = findProductById(productId);
		boolean check = product.getSize().contains(size);
		if(check) {
			throw new ProductException("product has this size");
		}
		product.getSize().add(size);
		return productMapper.toResponse(productRepository.save(product));
	}
	
	public Category findCategoryById(Long categoryId) throws CategoryException {
		Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
		return optionalCategory.orElseThrow(() -> new CategoryException("category not found"));
	}
	
	public Color findColorById(Long colorId) throws ColorException {
		Optional<Color> optionalColor = colorRepository.findById(colorId);
		return optionalColor.orElseThrow(() -> new ColorException("color not found"));
	}
	
	public Size findSizeById(Long sizeId) throws SizeException {
		Optional<Size> optionalSize = sizeRepository.findById(sizeId);
		return optionalSize.orElseThrow(() -> new SizeException("size not found"));
	}
	
	public ImageProduct findImageProductById(Long idImage) throws ImageProductException {
		Optional<ImageProduct> optionalImageProduct = iImageProductRepository.findById(idImage);
		return optionalImageProduct.orElseThrow(() -> new ImageProductException("image not found"));
	}
	
}
