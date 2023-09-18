package ra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.domain.Product;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {
	
	Page<Product> findAllByProductNameContaining(Pageable pageable, String productName);
	
	boolean existsByProductName(String productName);
	
}
