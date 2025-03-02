package tshirt.ecommerce.library.repository;


import org.springframework.stereotype.Repository;
import tshirt.ecommerce.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}