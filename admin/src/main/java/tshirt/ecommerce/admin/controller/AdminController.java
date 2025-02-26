package tshirt.ecommerce.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tshirt.ecommerce.library.model.Customer;
import tshirt.ecommerce.library.model.Product;
import tshirt.ecommerce.library.model.Role;
import tshirt.ecommerce.library.model.User;
import tshirt.ecommerce.library.repository.*;
import tshirt.ecommerce.library.service.OrderService;
import tshirt.ecommerce.library.web.dto.CounterSaleDto;
import tshirt.ecommerce.library.web.dto.CounterSaleProductDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequestMapping("/admin")
@Controller
public class AdminController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderService orderService;

    @RequestMapping(path = "/add-admin")
    public String addAdmin(Model model){
        User admin = new User();
        model.addAttribute("admin", admin);
        return "/admin/add-admin";
    }

    @PostMapping(path = "/save")
    public String saveAdmin(Model model, @ModelAttribute("admin") User admin){
        Role role = roleRepository.findByName("ADMIN");
        admin.setIsActive(true);
        admin.setRoles(Collections.singletonList(role));
        admin.setIsDeleted(false);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        userRepository.save(admin);
        return "redirect:/?success";
    }

    @GetMapping(path = "/tai-quay")
    public String taiQuay(Model model){
        CounterSaleDto counterSaleDto = new CounterSaleDto();
        List<CounterSaleProductDto> counterSaleProductDtos = new ArrayList<>();
        List<Customer> customers = customerRepository.findAll();
        List<Product> products = productRepository.findAll();
        products.forEach(product -> {
            CounterSaleProductDto counterSaleProductDto = new CounterSaleProductDto();
            counterSaleProductDto.setId(product.getId());
            counterSaleProductDto.setName(product.getName());
            counterSaleProductDto.setImage(product.getFullImage1Url());
            counterSaleProductDto.setPrice(product.getOurPrice());
            counterSaleProductDto.setQuantity(0);
            counterSaleProductDtos.add(counterSaleProductDto);
        });
        counterSaleDto.setProducts(counterSaleProductDtos);
        model.addAttribute("counterSaleDto", counterSaleDto);
        model.addAttribute("customers", customers);
        return "/admin/tai-quay";
    }

    @PostMapping(path = "/tai-quay/save")
    public String taoHoaDon(@ModelAttribute("counterSaleDto") CounterSaleDto counterSaleDto){
        orderService.createOrder(counterSaleDto);
        return "redirect:/order/list";

    }
}
