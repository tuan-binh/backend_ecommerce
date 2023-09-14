package ra.service.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.exception.RoleException;
import ra.model.domain.ERole;
import ra.model.domain.Roles;
import ra.repository.IRoleRepository;

import java.util.Optional;

@Service
public class RoleService implements IRoleService {
	
	@Autowired
	private IRoleRepository roleRepository;
	
	@Override
	public Roles findByRoleName(ERole roleName) throws RoleException {
		Optional<Roles> optionalRoles = roleRepository.findByRoleName(roleName);
		return optionalRoles.orElseThrow(() -> new RoleException("role not found"));
	}
}
