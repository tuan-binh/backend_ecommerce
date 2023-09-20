package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ra.exception.*;
import ra.model.dto.request.ImageRequest;
import ra.model.dto.request.ProductRequest;
import ra.model.dto.request.ProductUpdate;
import ra.model.dto.response.ImageResponse;
import ra.model.dto.response.ProductResponse;
import ra.service.product.IProductService;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/product")
@CrossOrigin("*")
public class ProductController {
	
	@Autowired
	private IProductService productService;
	
	
	@GetMapping("/get_all")
	public ResponseEntity<Page<ProductResponse>> getAllProducts(@PageableDefault(page = 0, size = 3) Pageable pageable, @RequestParam(value = "search", defaultValue = "") Optional<String> search) {
		return new ResponseEntity<>(productService.findAll(pageable, search), HttpStatus.OK);
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) throws ProductException {
		ProductResponse productResponse = productService.findById(id);
		return new ResponseEntity<>(productResponse, HttpStatus.OK);
	}
	
	@PostMapping("/add")
//	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<ProductResponse> handleAddProduct(@ModelAttribute ProductRequest productRequest) throws ProductException, CategoryException, ImageProductException {
		return new ResponseEntity<>(productService.save(productRequest), HttpStatus.CREATED);
	}
	
	@PutMapping("/update/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<ProductResponse> handleUpdateProduct(@RequestBody ProductUpdate productUpdate, @PathVariable Long id) throws CategoryException {
		return new ResponseEntity<>(productService.update(productUpdate, id), HttpStatus.OK);
	}
	
	@GetMapping("/change_status/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<ProductResponse> handleChangeStatusProduct(@PathVariable Long id) throws ProductException {
		return new ResponseEntity<>(productService.changeStatus(id), HttpStatus.OK);
	}
	
	@PutMapping("/add_image")
//	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<List<ImageResponse>> handleAddImageToProduct(@ModelAttribute ImageRequest imageRequest) throws ProductException, ColorException, CouponException, CategoryException, ProductDetailException, OrderException, SizeException, ImageProductException {
		return new ResponseEntity<>(productService.addImageToProduct(imageRequest), HttpStatus.CREATED);
	}
	
	@DeleteMapping("/delete_image/{idImage}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<ImageResponse> handleDeleteImageInProduct(@PathVariable Long idImage) throws ImageProductException, ProductException {
		return new ResponseEntity<>(productService.deleteImageInProduct(idImage), HttpStatus.OK);
	}
	
	@PostMapping("/add_category/{categoryId}/to/{productId}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<ProductResponse> handleAddCategoryToProduct(@PathVariable Long categoryId, @PathVariable Long productId) throws CategoryException, ProductException {
		return new ResponseEntity<>(productService.addCategoryToProduct(categoryId, productId), HttpStatus.CREATED);
	}
	
	@DeleteMapping("/remove_category/{productId}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<ProductResponse> handleRemoveCategoryInProduct(@PathVariable Long productId) throws ProductException {
		return new ResponseEntity<>(productService.removeCategoryInProduct(productId), HttpStatus.OK);
	}
	
}
