package tshirt.ecommerce.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tshirt.ecommerce.library.model.Material;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    Material findByName(String name);
}
