package ra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.domain.Color;

@Repository
public interface IColorRepository extends JpaRepository<Color, Long> {
}
