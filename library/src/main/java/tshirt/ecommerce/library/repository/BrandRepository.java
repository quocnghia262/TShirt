package tshirt.ecommerce.library.repository;


import tshirt.ecommerce.library.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Brand findByName(String name);
}