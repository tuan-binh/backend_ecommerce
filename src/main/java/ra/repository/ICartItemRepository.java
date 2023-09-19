package ra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.model.domain.CartItem;
import ra.model.domain.Orders;
import ra.model.domain.ProductDetail;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface ICartItemRepository extends JpaRepository<CartItem, Long> {
	
	Optional<CartItem> findCartItemByOrdersAndProductDetail(Orders orders, ProductDetail productDetail);
	
	@Transactional
	@Modifying
	@Query("delete from CartItem as c where c.orders.id = :#{#order.id} ")
	void resetCartItemByOrderId(@Param("order") Orders orders);
	
	Optional<CartItem> findByOrders(Orders orders);
	
}
