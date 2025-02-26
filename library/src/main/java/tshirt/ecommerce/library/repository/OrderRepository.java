package tshirt.ecommerce.library.repository;


import tshirt.ecommerce.library.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}