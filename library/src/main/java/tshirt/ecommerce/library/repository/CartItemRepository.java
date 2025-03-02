package tshirt.ecommerce.library.repository;


import org.springframework.stereotype.Repository;
import tshirt.ecommerce.library.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}