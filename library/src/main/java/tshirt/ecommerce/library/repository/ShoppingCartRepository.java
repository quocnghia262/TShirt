package tshirt.ecommerce.library.repository;


import org.springframework.stereotype.Repository;
import tshirt.ecommerce.library.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
}