package ra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ra.model.domain.Users;
import ra.model.dto.response.CountOrderByUser;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<Users, Long> {
	
	boolean existsByEmail(String email);
	
	boolean existsByPhone(String phone);
	
	Optional<Users> findByEmail(String email);
	
	Page<Users> findAllByFullNameContaining(Pageable pageable, String fullName);
	
	@Modifying
	@Query("SELECT new ra.model.dto.response.CountOrderByUser(u.fullName,COUNT(o.id)) " +
			  "FROM Users u JOIN u.orders o " +
			  "WHERE o.status = true " +
			  "GROUP BY u.id " +
			  "ORDER BY count(o.id) DESC")
	List<CountOrderByUser> getCountOrderByUser();
	
}
