package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.exception.UserException;
import ra.model.dto.response.UserResponse;
import ra.service.user.IUserService;

import java.util.Optional;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {
	
	@Autowired
	private IUserService userService;
	
	@GetMapping("/statistical/order")
	public ResponseEntity<?> getStatisticalOrder() {
		return new ResponseEntity<>(userService.handleStatistical(), HttpStatus.OK);
	}
	
	@GetMapping("/user/get_all")
	public ResponseEntity<Page<UserResponse>> getAllUsers(@PageableDefault(page = 0, size = 3) Pageable pageable, @RequestParam(value = "search", defaultValue = "") Optional<String> search) {
		return new ResponseEntity<>(userService.findAll(pageable, search), HttpStatus.OK);
	}
	
	@GetMapping("/user/get/{id}")
	public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) throws UserException {
		return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
	}
	
	@GetMapping("/user/change_status/{id}")
	public ResponseEntity<UserResponse> handleChangeStatus(@PathVariable Long id) throws UserException {
		return new ResponseEntity<>(userService.changeStatus(id), HttpStatus.OK);
	}
	
}
