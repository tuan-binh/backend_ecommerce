package ra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.domain.Size;

@Repository
public interface ISizeRepository extends JpaRepository<Size, Long> {
	Page<Size> findAllBySizeName(Pageable pageable, String sizeName);
	
}
