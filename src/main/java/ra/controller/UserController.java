package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

import java.util.Optional;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {
	
	@Autowired
	private IUserService userService;
	
	@PutMapping("/update_info")
	public ResponseEntity<UserResponse> handleUpdateInfo(@RequestBody UserUpdate userUpdate, Authentication authentication) throws UserException {
		return new ResponseEntity<>(userService.updateYourInfo(userUpdate, authentication), HttpStatus.OK);
	}
	
	@PutMapping("/change_password")
	public ResponseEntity<UserResponse> handleChangePassword(@RequestBody ChangePassword changePassword, Authentication authentication) throws UserException {
		return new ResponseEntity<>(userService.changePassword(changePassword, authentication), HttpStatus.OK);
	}
	
	
}
