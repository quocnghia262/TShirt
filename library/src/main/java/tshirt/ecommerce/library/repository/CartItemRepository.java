package tshirt.ecommerce.library.repository;


import tshirt.ecommerce.library.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}