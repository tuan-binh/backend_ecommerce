package ra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.domain.Coupon;

@Repository
public interface ICouponRepository extends JpaRepository<Coupon, Long> {
}
