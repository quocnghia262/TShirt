package tshirt.ecommerce.library.service;


import tshirt.ecommerce.library.model.Customer;
import tshirt.ecommerce.library.web.dto.CustomerRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface CustomerService extends UserDetailsService{
    Customer save(CustomerRegistrationDto registrationDto);//For insert
    Customer save(Customer customer);//For update
    Customer findByUsername(String username);
    Customer findById(Long id);
    List<Customer> findAll();

    List<Customer> topMostOrderedCustomers(int top);
}
