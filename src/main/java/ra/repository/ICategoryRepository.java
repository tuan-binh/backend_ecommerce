package ra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.domain.Category;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
}
