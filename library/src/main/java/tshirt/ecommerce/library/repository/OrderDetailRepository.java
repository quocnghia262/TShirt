package tshirt.ecommerce.library.repository;


import org.springframework.stereotype.Repository;
import tshirt.ecommerce.library.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

}