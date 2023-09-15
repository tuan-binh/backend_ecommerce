package ra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.domain.Color;

@Repository
public interface IColorRepository extends JpaRepository<Color, Long> {
	
	Page<Color> findAllByColorNameContaining(Pageable pageable, String colorName);
	
}
