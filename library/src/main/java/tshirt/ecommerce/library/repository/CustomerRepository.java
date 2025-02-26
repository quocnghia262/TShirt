package tshirt.ecommerce.library.repository;


import tshirt.ecommerce.library.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByUsername(String username);

    @Query(value = "SELECT cs.*, sum(sub_total) as total \n" +
            "from customer cs\n" +
            "inner join orders ord on cs.customer_id = ord.customer_id\n" +
            "inner join order_detail detail on ord.order_id = detail.order_id\n" +
            "group by cs.customer_id\n" +
            "order by total desc " +
            "limit ?1", nativeQuery = true )
    List<Customer> topMostOrderedCustomers(int top);
}