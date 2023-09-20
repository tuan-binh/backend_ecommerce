package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ra.exception.CategoryException;
import ra.model.dto.request.CategoryRequest;
import ra.model.dto.response.CategoryResponse;
import ra.service.category.ICategoryService;

import java.util.Optional;

@RestController
@RequestMapping("/category")
@CrossOrigin("*")
public class CategoryController {
	
	@Autowired
	private ICategoryService categoryService;
	
	@GetMapping("/get_all")
	public ResponseEntity<Page<CategoryResponse>> getAllCategory(@PageableDefault(page = 0, size = 1) Pageable pageable, @RequestParam(value = "search", defaultValue = "") Optional<String> search) {
		return new ResponseEntity<>(categoryService.findAll(pageable, search), HttpStatus.OK);
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) throws CategoryException {
		return new ResponseEntity<>(categoryService.findById(id), HttpStatus.OK);
	}
	
	@PostMapping("/add")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<CategoryResponse> handleAddCategory(@RequestBody CategoryRequest categoryRequest) throws CategoryException {
		return new ResponseEntity<>(categoryService.save(categoryRequest), HttpStatus.CREATED);
	}
	
	@PutMapping("/update/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<CategoryResponse> handleUpdateCategory(@RequestBody CategoryRequest categoryRequest, @PathVariable Long id) {
		return new ResponseEntity<>(categoryService.update(categoryRequest, id), HttpStatus.OK);
	}
	
	@GetMapping("/change_status/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<CategoryResponse> handleChangeStatus(@PathVariable Long id) throws CategoryException {
		return new ResponseEntity<>(categoryService.changeStatus(id), HttpStatus.OK);
	}
	
	
	
}
