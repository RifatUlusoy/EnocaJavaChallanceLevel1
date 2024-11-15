package experience.demo.Repository;

import experience.demo.Model.Customer;
import experience.demo.UserRole;
import jakarta.persistence.NamedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {


    Optional<Customer> findByEmail(String email);
    Boolean existsByEmail(String email);

    Customer findByUserRole(UserRole userRole);
}
