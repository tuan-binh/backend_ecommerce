package ra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.domain.Category;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
	Page<Category> findAllByCategoryNameContaining(Pageable pageable, String categoryName);
}
