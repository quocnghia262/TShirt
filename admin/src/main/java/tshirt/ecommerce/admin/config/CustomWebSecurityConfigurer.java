package tshirt.ecommerce.admin.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import tshirt.ecommerce.library.service.AddressService;
import tshirt.ecommerce.library.service.BrandService;
import tshirt.ecommerce.library.service.CategoryService;
import tshirt.ecommerce.library.service.ColorService;
import tshirt.ecommerce.library.service.CountryService;
import tshirt.ecommerce.library.service.CustomerService;
import tshirt.ecommerce.library.service.MaterialService;
import tshirt.ecommerce.library.service.OrderService;
import tshirt.ecommerce.library.service.ProductService;
import tshirt.ecommerce.library.service.SizeService;
import tshirt.ecommerce.library.service.StatisticService;
import tshirt.ecommerce.library.service.UserService;
import tshirt.ecommerce.library.service.impl.CustomerServiceImpl;
import tshirt.ecommerce.library.service.impl.UserServiceImpl;
import tshirt.ecommerce.library.util.Utility;

@Configuration
@EnableWebSecurity
public class CustomWebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Bean
    public CustomerService customerService() {
        return new CustomerServiceImpl();
    }

    @Bean
    public ProductService productService() {
        return new ProductService();
    }

    @Bean
    public CategoryService categoryService() {
        return new CategoryService();
    }

    @Bean
    public UserService userService() {
        return new UserServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public StatisticService statisticService() {
        return new StatisticService();
    }

    @Bean
    public OrderService orderService() {
        return new OrderService();
    }

    @Bean
    public AddressService addressService() {
        return new AddressService();
    }

    @Bean
    public BrandService modelService() {
        return new BrandService();
    }

    @Bean
    public CountryService countryService() {
        return new CountryService();
    }

    @Bean
    public ColorService colorService() {
        return new ColorService();
    }

    @Bean
    public MaterialService materialService() {
        return new MaterialService();
    }

    @Bean
    public SizeService sizeService() {
        return new SizeService();
    }

    @Bean
    public Utility utility() {
        return new Utility();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService());
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers(
                        "/register**",
                        "/dist/**"
                        , "/plugins/**"
                        , "/bootstrap/**"
                        , "/extra/**"
                        , "/upload/**"
                        , "/favicon.ico")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .permitAll()
                .and()
                .logout()
//                .invalidateHttpSession(true)
//                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll();
    }
}
