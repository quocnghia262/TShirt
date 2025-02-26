package tshirt.ecommerce.client.controller;


import org.springframework.web.bind.annotation.*;
import tshirt.ecommerce.library.model.Address;
import tshirt.ecommerce.library.model.Country;
import tshirt.ecommerce.library.model.Customer;
import tshirt.ecommerce.library.model.Order;
import tshirt.ecommerce.library.service.AddressService;
import tshirt.ecommerce.library.service.CountryService;
import tshirt.ecommerce.library.service.CustomerService;
import tshirt.ecommerce.library.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import tshirt.ecommerce.library.web.dto.AddressDto;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MyAccountController {
    @Autowired
    CustomerService customerService;

    @Autowired
    CountryService countryService;

    @Autowired
    OrderService orderService;

    @Autowired
    AddressService addressService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    //--------------- MY ACCOUNT ---------------------//
    @RequestMapping("/my-account")
    public String myAccount(Model model) {
        model.addAttribute("classActiveMyAccount", "home active");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        //Get logged in customer
        Customer customer = customerService.findByUsername(currentUserName);
        model.addAttribute("customer", customer);

        //Get countries list
        List<Country> countryList = countryService.findAll();
        model.addAttribute("countries", countryList);

        return "/my-account/my-account";
    }

    @PostMapping("/my-account")
    public String myAccountSave(@Valid @ModelAttribute("customer") Customer customer, BindingResult result, Model model) {
        model.addAttribute("classActiveMyAccount", "home active");

        //Get countries list
        List<Country> countryList = countryService.findAll();
        model.addAttribute("countries", countryList);

        //Get logged in customer
        model.addAttribute("customer", customer);

        if(result.hasErrors()){
            return "/my-account/my-account";
        }

        customerService.save(customer);

        return "redirect:/my-account?success";
    }
    //--------------- END MY ACCOUNT ---------------------//

    @RequestMapping("/order-history")
    public String orderHistory(Model model, Principal principal) {
        model.addAttribute("classActiveMyAccount", "home active");

        Customer customer = customerService.findByUsername(principal.getName());

        //customer orders
        model.addAttribute("orders", customer.getOrders().stream().sorted(Comparator.comparing(Order::getOrderDate, Comparator.reverseOrder())).collect(Collectors.toList()));

        return "/my-account/order-history";
    }
    @RequestMapping("/order-details")
    public String orderDetails(@RequestParam("id") Long id,
                               @RequestParam(name = "method", required = false, defaultValue = "") String method,
                               Principal principal, Model model) {
        model.addAttribute("classActiveMyAccount", "home active");

        Customer customer = customerService.findByUsername(principal.getName());

        Order order;
        try {
            order = orderService.get(id);

            if(method.equals("cancel")){
                order.setOrderStatus("Canceled");
                orderService.save(order);
                return "redirect:/order-history";
            }
            //invalid order id
            if(order == null){
                model.addAttribute("error", "Order does not exits.");
                return "/my-account/order-details";
            }
            if(!order.getCustomer().equals(customer)){
                model.addAttribute("error", "Order did not belongs to you.");
                return "/my-account/order-details";
            }

            model.addAttribute("order", order);
            return "/my-account/order-details";

        }catch (Exception ex){
            model.addAttribute("error", ex.getMessage());
            return "/my-account/order-details";
        }
    }

    @RequestMapping("/change-password")
    public String changePassword(Model model) {
        model.addAttribute("classActiveMyAccount", "home active");

        return "/my-account/change-password";
    }
    @PostMapping("/change-password")
    public String saveChangePassword(@Valid
                                         @ModelAttribute("old_password") String oldPassword
                                        , @ModelAttribute("new_password") String newPassword
                                        , @ModelAttribute("confirm_password") String confirm_password
                                        , Principal principal
                                        , BindingResult result
                                        , Model model) {
        model.addAttribute("classActiveMyAccount", "home active");

        Customer customer = customerService.findByUsername(principal.getName());

        if(bCryptPasswordEncoder.matches(oldPassword, customer.getPassword())){
            //match successfully
            String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
            customer.setPassword(encodedPassword);

            customerService.save(customer);
        }else {
            return "redirect:/change-password?error";
        }

        return "redirect:/change-password?success";
    }

    @RequestMapping("/add-address")
    public String addAddress(Model model) {
        AddressDto address = new AddressDto();
        model.addAttribute("address", address);
        model.addAttribute("countries", countryService.findAll());
        return "my-account/add-edit-address";
    }
    @RequestMapping("/address/edit/{id}")
    public String addAddress(Model model, @PathVariable("id") Long id) {
        Address foundAddress = addressService.findById(id);
        model.addAttribute("address", addressService.getAddressDto(foundAddress));
        model.addAttribute("countries", countryService.findAll());
        return "my-account/add-edit-address";
    }
    @RequestMapping("/add-address/save")
    public String saveAddress(@ModelAttribute("address") AddressDto addressDto, Principal principal) {
        Address address = addressService.getAddress(addressDto);
        address.setCustomer(customerService.findByUsername(principal.getName()));
        address.setIsDefault(false);
        Address savedAddress = addressService.save(address);
        return "redirect:/my-account";
    }


    @RequestMapping("/addresses")
    public String addresses(Model model, Principal principal) {
        Customer customer = customerService.findByUsername(principal.getName());
        model.addAttribute("addresses", customer.getAddresses());
        return "/my-account/addresses";
    }
}
