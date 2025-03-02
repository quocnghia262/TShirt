package tshirt.ecommerce.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tshirt.ecommerce.library.model.Color;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {
    Color findByName(String name);
}
