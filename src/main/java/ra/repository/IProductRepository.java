package ra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.domain.Product;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {
}
