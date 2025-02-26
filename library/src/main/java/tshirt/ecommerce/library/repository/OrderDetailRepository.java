package tshirt.ecommerce.library.repository;


import tshirt.ecommerce.library.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

}