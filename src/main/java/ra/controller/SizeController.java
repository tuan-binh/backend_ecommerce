package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ra.exception.SizeException;
import ra.model.dto.request.SizeRequest;
import ra.model.dto.response.SizeResponse;
import ra.service.size.ISizeService;

import java.util.Optional;

@RestController
@RequestMapping("/size")
@CrossOrigin("*")
public class SizeController {
	
	@Autowired
	private ISizeService sizeService;
	
	@GetMapping("/get_all")
	public ResponseEntity<Page<SizeResponse>> getAllSize(@PageableDefault(page = 0, size = 3) Pageable pageable, @RequestParam(value = "search", defaultValue = "") Optional<String> search) {
		return new ResponseEntity<>(sizeService.findAll(pageable, search), HttpStatus.OK);
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<SizeResponse> getSizeById(@PathVariable Long id) throws SizeException {
		return new ResponseEntity<>(sizeService.findById(id), HttpStatus.OK);
	}
	
	@PostMapping("/add")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<SizeResponse> handleAddSize(@RequestBody SizeRequest sizeRequest) throws SizeException {
		return new ResponseEntity<>(sizeService.save(sizeRequest), HttpStatus.CREATED);
	}
	
	@PutMapping("/update/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<SizeResponse> handleUpdateSize(@RequestBody SizeRequest sizeRequest, @PathVariable Long id) {
		return new ResponseEntity<>(sizeService.update(sizeRequest, id), HttpStatus.OK);
	}
	
	@GetMapping("/change_status/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<SizeResponse> handleChangeStatus(@PathVariable Long id) throws SizeException {
		return new ResponseEntity<>(sizeService.changeStatus(id), HttpStatus.OK);
	}
	
}
