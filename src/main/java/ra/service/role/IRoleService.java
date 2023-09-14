package ra.service.role;

import ra.exception.RoleException;
import ra.model.domain.ERole;
import ra.model.domain.Roles;

public interface IRoleService {
	Roles findByRoleName(ERole roleName) throws RoleException;
}
