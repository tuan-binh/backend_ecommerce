package ra.mapper.user;

import org.springframework.stereotype.Component;
import ra.mapper.IGenericMapper;
import ra.model.domain.Users;
import ra.model.dto.request.UserRegister;
import ra.model.dto.response.UserResponse;

import java.util.stream.Collectors;

@Component
public class UserMapper implements IGenericMapper<Users, UserRegister, UserResponse> {
	@Override
	public Users toEntity(UserRegister userRegister) {
		return Users.builder()
				  .fullName(userRegister.getFullName())
				  .email(userRegister.getEmail())
				  .password(userRegister.getPassword())
				  .build();
	}
	
	@Override
	public UserResponse toResponse(Users users) {
		return UserResponse.builder()
				  .id(users.getId())
				  .fullName(users.getFullName())
				  .email(users.getEmail())
				  .phone(users.getPhone())
				  .address(users.getAddress())
				  .roles(users.getRoles().stream().map(item -> item.getRoleName().name()).collect(Collectors.toSet()))
				  .favourites(users.getFavourites())
				  .rates(users.getRates())
				  .orders(users.getOrders())
				  .status(users.isStatus())
				  .build();
	}
}
