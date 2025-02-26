package tshirt.ecommerce.library.service.impl;


import tshirt.ecommerce.library.model.Address;
import tshirt.ecommerce.library.model.Country;
import tshirt.ecommerce.library.model.Customer;
import tshirt.ecommerce.library.model.Role;
import tshirt.ecommerce.library.repository.CountryRepository;
import tshirt.ecommerce.library.repository.CustomerRepository;
import tshirt.ecommerce.library.repository.RoleRepository;
import tshirt.ecommerce.library.service.CustomerService;
import tshirt.ecommerce.library.web.dto.CustomerRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public CustomerServiceImpl() {
        super();
    }

    @Override
    public Customer save(CustomerRegistrationDto registrationDto) {

        //Creating admin role user
        Customer customer = new Customer();
        customer.setFirstName(registrationDto.getFirstName());
        customer.setLastName(registrationDto.getLastName());
        customer.setUsername(registrationDto.getUsername());
        customer.setPhone(registrationDto.getPhone());
        customer.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        customer.setRoles(Arrays.asList(roleRepository.findByName("CUSTOMER")));
        customer.setIsDeleted(false);

        //Get country
        Country country = countryRepository.findByName(registrationDto.getCountry());

        //Address
        Address address = new Address();
        address.setCompany(registrationDto.getCompany());
        address.setAddress1(registrationDto.getAddress1());
        address.setAddress2(registrationDto.getAddress2());
        address.setPostalCode(registrationDto.getPostalCode());
        address.setCity(registrationDto.getCity());
        address.setCountry(country);
        address.setState(registrationDto.getState());
        address.setIsDefault(true);

        customer.setAddresses(Collections.singletonList(address));

        return customerRepository.save(customer);
    }

    @Override
    public Customer findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    @Override
    public Customer findById(Long id) {
        return customerRepository.findById(id).get();
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public List<Customer> topMostOrderedCustomers(int top) {
        return customerRepository.topMostOrderedCustomers(top);
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Customer customer = customerRepository.findByUsername(username);
        if(customer == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(customer.getUsername(), customer.getPassword()
                , mapRolesToAuthorities(customer.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

}
