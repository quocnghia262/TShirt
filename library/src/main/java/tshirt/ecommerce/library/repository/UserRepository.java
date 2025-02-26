package tshirt.ecommerce.library.repository;


import tshirt.ecommerce.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}