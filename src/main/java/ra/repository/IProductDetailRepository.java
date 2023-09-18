package ra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.domain.ProductDetail;

@Repository
public interface IProductDetailRepository extends JpaRepository<ProductDetail, Long> {
}
