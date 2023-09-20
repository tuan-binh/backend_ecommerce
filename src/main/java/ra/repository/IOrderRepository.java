package ra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.domain.Orders;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderRepository extends JpaRepository<Orders, Long> {
	Page<Orders> findAllByPhone(Pageable pageable, String phone);
	
	List<Orders> findAllByUsersIdAndStatus(Long userId, boolean status);
	
	Optional<Orders> findByUsersIdAndStatus(Long userId, boolean status);
	
	Optional<Orders> findByIdAndUsersId(Long orderId, Long userId);
	
	int countOrdersByUsersIdAndStatus(Long id, boolean status);
	
}
