package tshirt.ecommerce.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tshirt.ecommerce.library.model.Size;

@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {
    Size findByName(String name);
}
