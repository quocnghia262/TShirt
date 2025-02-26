package tshirt.ecommerce.admin;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tshirt.ecommerce.library.model.*;
import tshirt.ecommerce.library.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Component
public class SetupData implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (alreadySetup)
            return;

        //Setup roles
        createRoleIfNotFound("ADMIN");
        createRoleIfNotFound("CUSTOMER");

        //Setup categories
        setupCategories();

        //Setup brands
        setupBrands();

        //Setup users
        setupUsers();

        //Setup countries
        setupCountries();
    }
    @Transactional
    public void setupCountries(){
        Country country = new Country();
        country.setName("Viet Nam");
        country.setCode("1");
        createCountryIfNotFound(country);

        Country country1 = new Country();
        country1.setName("My");
        country.setCode("2");
        createCountryIfNotFound(country1);

        Country country2 = new Country();
        country2.setName("Singapore");
        country.setCode("3");
        createCountryIfNotFound(country2);
    }

    @Transactional
    public void setupUsers(){
        Role admin = roleRepository.findByName("ADMIN");
        Role customer = roleRepository.findByName("CUSTOMER");

        User u1 = new User();
        u1.setFirstName("Admin");
        u1.setLastName("Admin");
        u1.setUsername("admin@viet.dev");
        u1.setPassword("$2a$12$RojzSeIAcZzdogtMua/07uxxm94P5k1zzRhVvk0zUGFpFTOGEgFgS");
        u1.setIsActive(true);
        u1.setIsDeleted(false);
        u1.setRoles(Collections.singletonList(admin));
        createUserIfNotFound(u1);

        User u2 = new User();
        u2.setFirstName("Admeo");
        u2.setLastName("Admeo");
        u2.setUsername("admeo@viet.dev");
        u2.setPassword(passwordEncoder.encode("admeo"));
        u2.setIsActive(true);
        u2.setIsDeleted(false);
        u2.setRoles(Collections.singletonList(admin));
        createUserIfNotFound(u2);
    }

    @Transactional
    public void setupBrands(){
        Brand b1 = new Brand();
        b1.setName("Dior");
        b1.setIsDeleted(false);
        createBrandIfNotFound(b1);

        Brand b2 = new Brand();
        b2.setName("Versace");
        b2.setIsDeleted(false);
        createBrandIfNotFound(b2);

        Brand b3 = new Brand();
        b3.setName("Adidas");
        b3.setIsDeleted(false);
        createBrandIfNotFound(b3);

        Brand b4 = new Brand();
        b4.setName("Gucci");
        b4.setIsDeleted(false);
        createBrandIfNotFound(b4);

        Brand b5 = new Brand();
        b5.setName("Calvin Klevin");
        b5.setIsDeleted(false);
        createBrandIfNotFound(b5);

        Brand b6 = new Brand();
        b6.setName("Nike");
        b6.setIsDeleted(false);
        createBrandIfNotFound(b6);

        Brand b7 = new Brand();
        b7.setName("Puma");
        b7.setIsDeleted(false);
        createBrandIfNotFound(b7);

        Brand b8 = new Brand();
        b8.setName("Louis Vuitton");
        b8.setIsDeleted(false);
        createBrandIfNotFound(b8);
    }

    @Transactional
    public void setupCategories(){
        Category c1 = new Category();
        c1.setName("Nam");
        c1.setDescription("Áo phông dành cho nam giới");
        c1.setIsActive(true);
        c1.setIsDeleted(false);
        createCategoryIfNotFound(c1);

        Category c2 = new Category();
        c2.setName("Nữ");
        c2.setDescription("Áo phông dành cho nữ giới");
        c2.setIsActive(true);
        c2.setIsDeleted(false);
        createCategoryIfNotFound(c2);

        Category c3 = new Category();
        c3.setName("Unisex");
        c3.setDescription("Áo phông dành cho cả nam và nữ giới");
        c3.setIsActive(true);
        c3.setIsDeleted(false);
        createCategoryIfNotFound(c3);
    }

    @Transactional
    public Role createRoleIfNotFound(String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            roleRepository.save(role);

            //return saved object
            role = roleRepository.findByName(name);
        }
        return role;
    }

    @Transactional
    public Category createCategoryIfNotFound(Category category) {
        Category category1 = categoryRepository.findByName(category.getName());
        if (category1 == null) {
            categoryRepository.save(category);

            //return saved object
            category1 = categoryRepository.findByName(category.getName());
        }
        return category1;
    }

    @Transactional
    public Brand createBrandIfNotFound(Brand brand) {
        Brand brand1 = brandRepository.findByName(brand.getName());
        if (brand1 == null) {
            brandRepository.save(brand);

            //return saved object
            brand1 = brandRepository.findByName(brand.getName());
        }
        return brand1;
    }

    @Transactional
    public User createUserIfNotFound(User user) {
        User user1 = userRepository.findByUsername(user.getUsername());
        if (user1 == null) {
            user1 = userRepository.save(user);
        }
        return user1;
    }

    @Transactional
    public Country createCountryIfNotFound(Country country) {
        Country country1 = countryRepository.findByName(country.getName());
        if (country1 == null) {
            country1 = countryRepository.save(country);
        }
        return country1;
    }
}
