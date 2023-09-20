package ra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ra.model.domain.Users;
import ra.model.dto.response.CountOrderByUser;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<Users, Long> {
	
	boolean existsByEmail(String email);
	
	boolean existsByPhone(String phone);
	
	Optional<Users> findByEmail(String email);
	
	Page<Users> findAllByFullNameContaining(Pageable pageable, String fullName);


//	@Query("select u.fullName,u.email,count(o.id) from Users as u join Orders as o where u.id = o.users.id and o.status = true group by u.id order by count(o.id) desc ")
//	Optional<CountOrderByUser> getCountOrderByUser();
	
}
