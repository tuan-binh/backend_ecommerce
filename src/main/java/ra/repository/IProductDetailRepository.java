package ra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.domain.Product;
import ra.model.domain.ProductDetail;

import java.util.List;

@Repository
public interface IProductDetailRepository extends JpaRepository<ProductDetail, Long> {
	
	List<ProductDetail> findAllByProduct(Product product);
	
}
