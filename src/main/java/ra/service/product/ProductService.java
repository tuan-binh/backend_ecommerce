package ra.service.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ra.exception.*;
import ra.mapper.image.ImageMapper;
import ra.mapper.product.ProductMapper;
import ra.model.domain.Category;
import ra.model.domain.ImageProduct;
import ra.model.domain.Product;
import ra.model.dto.request.ImageRequest;
import ra.model.dto.request.ProductRequest;
import ra.model.dto.request.ProductUpdate;
import ra.model.dto.response.ImageResponse;
import ra.model.dto.response.ProductResponse;
import ra.repository.ICategoryRepository;
import ra.repository.IImageProductRepository;
import ra.repository.IProductRepository;
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
	private ICategoryRepository categoryRepository;
	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private ImageMapper imageMapper;
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
	}
	
	@Override
	public ProductResponse save(ProductRequest productRequest) throws ProductException, CategoryException, ImageProductException {
		Product product = productMapper.toEntity(productRequest);
		if (productRepository.existsByProductName(product.getProductName())) {
			throw new ProductException("exists product name");
		}
		List<String> listUrl = new ArrayList<>();
		for (MultipartFile m : productRequest.getFile()) {
			if (m.isEmpty()) {
				throw new ImageProductException("You must be upload load image");
			}
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
		// khởi tạo dối tượng để khi mapper  sang đỡ lỗi
		product.setRates(new ArrayList<>());
		return productMapper.toResponse(productRepository.save(product));
	}
	
	@Override
	public ProductResponse update(ProductUpdate productUpdate, Long id) throws CategoryException {
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
	
	@Override
	public ImageResponse changeImageAvatar(Long imageId, Long productId) throws ImageProductException, ProductException {
		ImageProduct imageProduct = findImageById(imageId);
		if (!Objects.equals(imageProduct.getProduct().getId(), productId)) {
			throw new ImageProductException("This photo does not belong to this product");
		}
		Product product = findProductById(productId);
		imageProduct.setProduct(product);
		return imageMapper.toResponse(iImageProductRepository.save(imageProduct));
	}
	
	@Override
	public List<ImageResponse> getImageByProductId(Long productId) {
		List<ImageProduct> list = iImageProductRepository.findAllByProductId(productId);
		return list.stream().map(item -> imageMapper.toResponse(item)).collect(Collectors.toList());
	}
	
	public ImageProduct findImageById(Long imageId) throws ImageProductException {
		Optional<ImageProduct> optionalImageProduct = iImageProductRepository.findById(imageId);
		return optionalImageProduct.orElseThrow(() -> new ImageProductException("image not found"));
	}
	
	public Product findProductById(Long id) throws ProductException {
		Optional<Product> optionalProduct = productRepository.findById(id);
		return optionalProduct.orElseThrow(() -> new ProductException("product not found"));
	}
	
	@Override
	public List<ImageResponse> addImageToProduct(ImageRequest imageRequest) throws ProductException, ColorException, CouponException, CategoryException, ProductDetailException, OrderException, SizeException, ImageProductException {
		ImageProduct imageProduct = imageMapper.toEntity(imageRequest);
		List<String> listUrl = new ArrayList<>();
		for (MultipartFile m : imageRequest.getImage()) {
			if (m.isEmpty()) {
				throw new ImageProductException("You must be upload load image");
			}
			listUrl.add(storageService.uploadFile(m));
		}
		List<ImageProduct> list = new ArrayList<>();
		for (String s : listUrl) {
			list.add(ImageProduct.builder().image(s).product(imageProduct.getProduct()).build());
		}
		iImageProductRepository.saveAll(list);
		return list.stream().map(item -> imageMapper.toResponse(item)).collect(Collectors.toList());
	}
	
	@Override
	public ImageResponse deleteImageInProduct(Long idImage) throws ImageProductException, ProductException {
		Optional<ImageProduct> optionalImageProduct = iImageProductRepository.findById(idImage);
		if (optionalImageProduct.isPresent()) {
			iImageProductRepository.deleteById(idImage);
			return imageMapper.toResponse(optionalImageProduct.get());
		}
		throw new ImageProductException("image not found");
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
	
	public Category findCategoryById(Long categoryId) throws CategoryException {
		Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
		return optionalCategory.orElseThrow(() -> new CategoryException("category not found"));
	}

//	public Color findColorById(Long colorId) throws ColorException {
//		Optional<Color> optionalColor = colorRepository.findById(colorId);
//		return optionalColor.orElseThrow(() -> new ColorException("color not found"));
//	}
//
//	public Size findSizeById(Long sizeId) throws SizeException {
//		Optional<Size> optionalSize = sizeRepository.findById(sizeId);
//		return optionalSize.orElseThrow(() -> new SizeException("size not found"));
//	}
//
//	public ImageProduct findImageProductById(Long idImage) throws ImageProductException {
//		Optional<ImageProduct> optionalImageProduct = iImageProductRepository.findById(idImage);
//		return optionalImageProduct.orElseThrow(() -> new ImageProductException("image not found"));
//	}
	

}
