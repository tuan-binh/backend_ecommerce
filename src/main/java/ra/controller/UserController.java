package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ra.exception.UserException;
import ra.model.dto.request.ChangePassword;
import ra.model.dto.request.UserUpdate;
import ra.model.dto.response.UserResponse;
import ra.service.user.IUserService;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {
	
	@Autowired
	private IUserService userService;
	
	@PutMapping("/update_info")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<UserResponse> handleUpdateInfo(@RequestBody @Valid UserUpdate userUpdate, Authentication authentication) throws UserException {
		return new ResponseEntity<>(userService.updateYourInfo(userUpdate, authentication), HttpStatus.OK);
	}
	
	@PutMapping("/change_password")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<UserResponse> handleChangePassword(@RequestBody @Valid ChangePassword changePassword, Authentication authentication) throws UserException {
		return new ResponseEntity<>(userService.changePassword(changePassword, authentication), HttpStatus.OK);
	}
	
	@GetMapping("/get_new_password")
	public ResponseEntity<String> handleGetNewPassWord(@RequestParam(value = "email", defaultValue = "") String email) throws UserException, MessagingException {
		userService.getNewPasswordWithEmail(email);
		return new ResponseEntity<>("Please check your email", HttpStatus.OK);
	}
	
}
