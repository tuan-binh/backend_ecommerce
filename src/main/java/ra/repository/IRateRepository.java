package ra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.domain.Rates;

@Repository
public interface IRateRepository extends JpaRepository<Rates, Long> {

}
