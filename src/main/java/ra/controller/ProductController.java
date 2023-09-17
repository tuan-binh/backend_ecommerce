package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ra.exception.ImageProductException;
import ra.exception.ProductException;
import ra.model.dto.request.ProductRequest;
import ra.model.dto.request.ProductUpdate;
import ra.model.dto.response.ProductResponse;
import ra.service.product.IProductService;


@RestController
@RequestMapping("/product")
@CrossOrigin("*")
public class ProductController {
	
	@Autowired
	private IProductService productService;
	
	
	@GetMapping("/get_all")
	public ResponseEntity<Page<ProductResponse>> getAllProducts(@PageableDefault(page = 0, size = 3) Pageable pageable) {
		return new ResponseEntity<>(productService.findAll(pageable), HttpStatus.OK);
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) throws ProductException {
		ProductResponse productResponse = productService.findById(id);
		return new ResponseEntity<>(productResponse, HttpStatus.OK);
	}
	
	@PostMapping("/add")
	public ResponseEntity<ProductResponse> handleAddProduct(@ModelAttribute ProductRequest productRequest) {
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
	
	
}
