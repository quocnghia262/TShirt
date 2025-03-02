package tshirt.ecommerce.library.repository;


import org.springframework.stereotype.Repository;
import tshirt.ecommerce.library.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    Brand findByName(String name);
}