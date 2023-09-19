package ra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.domain.Orders;

@Repository
public interface IOrderRepository extends JpaRepository<Orders, Long> {
	Page<Orders> findAllByPhone(Pageable pageable, String phone);
	
	Orders findOrdersByUsersIdAndStatus(Long id, boolean status);
	
	
	
}
