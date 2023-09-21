package ra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.domain.ImageProduct;

import java.util.List;

@Repository
public interface IImageProductRepository extends JpaRepository<ImageProduct, Long> {
	
	List<ImageProduct> findAllByProductId(Long productId);
	
}
