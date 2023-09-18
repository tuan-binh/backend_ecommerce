package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ra.exception.ProductException;
import ra.exception.UserException;
import ra.model.dto.response.ProductResponse;
import ra.service.favourite.IFavouriteService;

import java.util.List;

@RestController
@RequestMapping("/favourite")
@CrossOrigin("*")
public class FavouriteController {
	
	@Autowired
	private IFavouriteService favouriteService;
	
	@GetMapping("/getFavourite")
	public ResponseEntity<List<ProductResponse>> getFavourite(Authentication authentication) throws UserException {
		return new ResponseEntity<>(favouriteService.getFavourite(authentication), HttpStatus.OK);
	}
	
	@PostMapping("/favourite/{productId}")
	public ResponseEntity<List<ProductResponse>> handleAddProductToFavourite(@PathVariable Long productId, Authentication authentication) throws UserException, ProductException {
		return new ResponseEntity<>(favouriteService.addProductToFavourite(productId, authentication), HttpStatus.CREATED);
	}
	
	@DeleteMapping("/favourite/{productId}")
	public ResponseEntity<List<ProductResponse>> handleRemoveProductInFavourite(@PathVariable Long productId, Authentication authentication) throws UserException, ProductException {
		return new ResponseEntity<>(favouriteService.removeProductInFavourite(productId, authentication), HttpStatus.OK);
	}
	
	
}
