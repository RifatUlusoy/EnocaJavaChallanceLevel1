package experience.demo.Repository;

import experience.demo.Model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface  CartRepository extends JpaRepository<Cart,Long> {



    Optional<Cart> findByCustomerIdAndOrderId(Long customerId, Long orderId);



}
