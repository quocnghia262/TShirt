package tshirt.ecommerce.library.repository;


import tshirt.ecommerce.library.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}