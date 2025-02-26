package tshirt.ecommerce.library.service;


import tshirt.ecommerce.library.model.User;
import tshirt.ecommerce.library.web.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService{
    User save(UserRegistrationDto registrationDto);
    User findByUsername(String username);

    List<User> findAll();
}
