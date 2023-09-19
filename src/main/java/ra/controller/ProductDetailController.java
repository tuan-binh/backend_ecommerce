package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.exception.ColorException;
import ra.exception.ProductDetailException;
import ra.exception.ProductException;
import ra.exception.SizeException;
import ra.model.dto.request.ProductDetailRequest;
import ra.model.dto.response.ProductDetailResponse;
import ra.service.productDetail.IProductDetailService;

import java.util.List;

@RestController
@RequestMapping("/productDetail")
@CrossOrigin("*")
public class ProductDetailController {
	
	@Autowired
	private IProductDetailService productDetailService;
	
	@GetMapping("/get_all/{productId}")
	public ResponseEntity<List<ProductDetailResponse>> getAllProductDetailByProduct(@PathVariable Long productId) throws ProductException {
		return new ResponseEntity<>(productDetailService.findAllProductDetailByProduct(productId), HttpStatus.OK);
	}
	
	@PostMapping("/add_product_detail")
	public ResponseEntity<ProductDetailResponse> handleAddProductDetail(@RequestBody ProductDetailRequest productDetailRequest) throws ColorException, ProductException, SizeException, ProductDetailException {
		return new ResponseEntity<>(productDetailService.addProductDetailToProduct(productDetailRequest), HttpStatus.CREATED);
	}
	
	@PutMapping("/update/{productDetailId}")
	public ResponseEntity<ProductDetailResponse> handleAddStockToProductDetail(@RequestBody ProductDetailRequest productDetailRequest, @PathVariable Long productDetailId) throws ColorException, ProductException, SizeException, ProductDetailException {
		return new ResponseEntity<>(productDetailService.addStockToProductDetail(productDetailRequest, productDetailId), HttpStatus.OK);
	}
	
}
