package experience.demo.Repository;

import experience.demo.Model.Order;
import experience.demo.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {


    Order findByCustomerIdAndOrderStatus(Long customerId, OrderStatus orderStatus);

    List<Order> findAllByCustomerIdAndOrderStatus(Long customerId, OrderStatus orderStatus);

    List<Order> findAllByOrderStatus(OrderStatus orderStatus);




    Optional<Order> findByOrderCode(String orderCode);
}
