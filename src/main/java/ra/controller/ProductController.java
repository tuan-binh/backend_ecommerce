package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ra.exception.CategoryException;
import ra.exception.ImageProductException;
import ra.exception.ProductException;
import ra.model.dto.request.ProductDetailRequest;
import ra.model.dto.request.ProductRequest;
import ra.model.dto.request.ProductUpdate;
import ra.model.dto.response.ProductDetailResponse;
import ra.model.dto.response.ProductResponse;
import ra.service.product.IProductService;

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
	public ResponseEntity<ProductResponse> handleAddProduct(@ModelAttribute ProductRequest productRequest) throws ProductException {
		return new ResponseEntity<>(productService.save(productRequest), HttpStatus.CREATED);
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<ProductResponse> handleUpdateProduct(@RequestBody ProductUpdate productUpdate, @PathVariable Long id) {
		return new ResponseEntity<>(productService.update(productUpdate, id), HttpStatus.OK);
	}
	
	@GetMapping("/change_status/{id}")
	public ResponseEntity<ProductResponse> handleChangeStatusProduct(@PathVariable Long id) throws ProductException {
		return new ResponseEntity<>(productService.changeStatus(id), HttpStatus.OK);
	}
	
	@PutMapping("/add_image/to_product/{id}")
	public ResponseEntity<ProductResponse> handleAddImageToProduct(@RequestParam("file") MultipartFile multipartFile, @PathVariable Long id) throws ProductException {
		return new ResponseEntity<>(productService.addImageToProduct(multipartFile, id), HttpStatus.CREATED);
	}
	
	@DeleteMapping("/delete_image/{idImage}/in_product/{idProduct}")
	public ResponseEntity<ProductResponse> handleDeleteImageInProduct(@PathVariable Long idImage, @PathVariable Long idProduct) throws ImageProductException, ProductException {
		return new ResponseEntity<>(productService.deleteImageInProduct(idImage, idProduct), HttpStatus.OK);
	}

//	@PostMapping("/add_color/{colorId}/to/{productId}")
//	public ResponseEntity<ProductResponse> handleAddColorToProduct(@PathVariable Long colorId, @PathVariable Long productId) throws ColorException, ProductException {
//		return new ResponseEntity<>(productService.addColorToProduct(colorId, productId), HttpStatus.CREATED);
//	}
//
//	@DeleteMapping("/delete_color/{productId}/color/{colorId}")
//	public ResponseEntity<ProductResponse> handleDeleteColor(@PathVariable Long productId, @PathVariable Long colorId) throws ColorException, ProductException {
//		return new ResponseEntity<>(productService.deleteColorInProduct(productId, colorId), HttpStatus.OK);
//	}
//
//	@PostMapping("/add_size/{sizeId}/to/{productId}")
//	public ResponseEntity<ProductResponse> handleAddSizeToProduct(@PathVariable Long sizeId, @PathVariable Long productId) throws SizeException, ProductException {
//		return new ResponseEntity<>(productService.addSizeToProduct(sizeId, productId), HttpStatus.CREATED);
//	}
//
//
//	@DeleteMapping("/delete_size/{productId}/size/{sizeId}")
//	public ResponseEntity<ProductResponse> handleDeleteSize(@PathVariable Long productId, @PathVariable Long sizeId) throws ProductException, SizeException {
//		return new ResponseEntity<>(productService.deleteSizeInProduct(productId, sizeId), HttpStatus.OK);
//	}
//
	
	@PostMapping("/add_product_detail")
	public ResponseEntity<ProductDetailResponse> handleAddProductDetail(@ModelAttribute ProductDetailRequest productDetailRequest) {
		return new ResponseEntity<>(productService.addProductDetailToProduct(productDetailRequest), HttpStatus.CREATED);
	}
	
	@PostMapping("/add_category/{categoryId}/to/{productId}")
	public ResponseEntity<ProductResponse> handleAddCategoryToProduct(@PathVariable Long categoryId, @PathVariable Long productId) throws CategoryException, ProductException {
		return new ResponseEntity<>(productService.addCategoryToProduct(categoryId, productId), HttpStatus.CREATED);
	}
	
	@DeleteMapping("/remove_category/{productId}")
	public ResponseEntity<ProductResponse> handleRemoveCategoryInProduct(@PathVariable Long productId) throws ProductException {
		return new ResponseEntity<>(productService.removeCategoryInProduct(productId), HttpStatus.OK);
	}
	
}
