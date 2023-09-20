package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ra.exception.ColorException;
import ra.model.dto.request.ColorRequest;
import ra.model.dto.response.ColorResponse;
import ra.service.color.IColorService;

import java.util.Optional;

@RestController
@RequestMapping("/color")
@CrossOrigin("*")
public class ColorController {
	
	@Autowired
	private IColorService colorService;
	
	@GetMapping("/get_all")
	public ResponseEntity<Page<ColorResponse>> getAllColor(@PageableDefault(page = 0, size = 3) Pageable pageable, @RequestParam(value = "search", defaultValue = "") Optional<String> search) {
		return new ResponseEntity<>(colorService.findAll(pageable, search), HttpStatus.OK);
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<ColorResponse> getColorResponse(@PathVariable Long id) throws ColorException {
		return new ResponseEntity<>(colorService.findById(id), HttpStatus.OK);
	}
	
	@PostMapping("/add")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<ColorResponse> handleAddColor(@RequestBody ColorRequest colorRequest) throws ColorException {
		return new ResponseEntity<>(colorService.save(colorRequest), HttpStatus.CREATED);
	}
	
	@PutMapping("/update/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<ColorResponse> handleUpdateColor(@RequestBody ColorRequest colorRequest, @PathVariable Long id) {
		return new ResponseEntity<>(colorService.update(colorRequest, id), HttpStatus.OK);
	}
	
	@GetMapping("/change_status/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<ColorResponse> handleChangeStatusColor(@PathVariable Long id) throws ColorException {
		return new ResponseEntity<>(colorService.changeStatus(id), HttpStatus.OK);
	}
	
	
}
