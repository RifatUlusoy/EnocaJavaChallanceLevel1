package experience.demo.Repository;

import experience.demo.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    Boolean existsByName(String name);
    List<Product> findAllByNameContaining(String name);


    Optional<Product> findById(Long productId);
}
