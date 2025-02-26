package tshirt.ecommerce.client.controller;


import tshirt.ecommerce.client.config.VNPayConfig;
import tshirt.ecommerce.client.dto.PaymentInfoDTO;
import tshirt.ecommerce.library.model.*;
import tshirt.ecommerce.library.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ShoppingCartController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AddressService addressService;

    //----------------- CART ---------------//
    @RequestMapping("/view-cart")
    public String viewCart(Model model, Principal principal) {
        model.addAttribute("classActiveViewCart", "home active");

        Customer customer = customerService.findByUsername(principal.getName());//get logged in user
        ShoppingCart shoppingCart = customer.getShoppingCart();

        model.addAttribute("shoppingCart", shoppingCart);

        return "/client/view-cart";
    }

    @RequestMapping("/add-to-cart")
    public String addToCart(
            @ModelAttribute("id") Long id
            , @ModelAttribute("quantity") Long quantity
            , @ModelAttribute("size") String size
            , Model model
            , Principal principal) {
        model.addAttribute("classActiveViewCart", "home active");

        //Find product
        Product product = productService.get(id);

        if(quantity > product.getStockQty()){
            return "redirect:/part-details?id=" + product.getId() + "&error";
        }
        //Find customer
        Customer customer = customerService.findByUsername(principal.getName());//get logged in user

        //Add item to shopping cart
        shoppingCartService.addItemToCart(product, quantity, size, customer);

        return "redirect:/part-details?id=" + product.getId() + "&addtocart";
    }

    @RequestMapping("/empty-cart")
    public String emptyCart(Model model, Principal principal) {
        model.addAttribute("classActiveViewCart", "home active");

        Customer customer = customerService.findByUsername(principal.getName());//get logged in user

        shoppingCartService.emptyShoppingCart(customer);

        model.addAttribute("shoppingCart", customer.getShoppingCart());

        //Set message
        //model.addAttribute("removeCartMessage", "Cart has been empty successfully.");

        return "redirect:/view-cart?emptyCart";
    }

    @RequestMapping(value = "/update-cart", method = RequestMethod.POST, params = "action=update")
    public String updateCart(
            @ModelAttribute("id") Long id
            , @ModelAttribute("quantity") String quantity
            , Model model
            , Principal principal) {
        model.addAttribute("classActiveViewCart", "home active");

        //Check if valid quantity then use it otherwise default value is 1
        Long qty = 1L;
        try {
            qty = Long.parseLong(quantity);
        } catch (NumberFormatException ex) {
            qty = 1L;
        }

        //Find product
        Product product = productService.get(id);

        Customer customer = customerService.findByUsername(principal.getName());//get logged in user

        shoppingCartService.updateItemFromCart(product, qty, customer);

        model.addAttribute("shoppingCart", customer.getShoppingCart());

        return "redirect:/view-cart?updateCart";
    }

    @RequestMapping(value = "/update-cart", method = RequestMethod.POST, params = "action=delete")
    public String removeCart(
            @ModelAttribute("id") Long id
            , Model model
            , Principal principal) {
        model.addAttribute("classActiveViewCart", "home active");

        //Find product
        Product product = productService.get(id);

        Customer customer = customerService.findByUsername(principal.getName());//get logged in user

        shoppingCartService.removeItemFromCart(product, customer);

        model.addAttribute("shoppingCart", customer.getShoppingCart());

        return "redirect:/view-cart?removeCart";
    }
    //----------------- END CART ---------------//


    //--------------- END CHECKOUT ---------------------//
    @RequestMapping("/checkout")
    public String checkout(Model model) {
        model.addAttribute("classActiveCheckout", "home active");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        Customer customer = customerService.findByUsername(currentUserName);

        //Get shopping cart
        model.addAttribute("shoppingCart", customer.getShoppingCart());

        //Get countries list
        List<Country> countryList = countryService.findAll();
        model.addAttribute("countries", countryList);

        List<Address> addresses = customer.getAddresses();
        model.addAttribute("addresses", addresses);

        return "/client/checkout";
    }

    @PostMapping("/checkout")
    public String myAccountSave(@Valid
                                @ModelAttribute("shoppingCart") ShoppingCart shoppingCart
                                , @ModelAttribute("selectedAddressId") Long selectedAddressId
            , Principal principal
            , BindingResult result
            , HttpServletRequest request
            , Model model
            , HttpServletResponse response) throws IOException {
        model.addAttribute("classActiveCheckout", "home active");

        Address shippingAddress = addressService.findById(selectedAddressId);
        //Get countries list
        List<Country> countryList = countryService.findAll();

        //set countries
        model.addAttribute("countries", countryList);

        //Get full object
        ShoppingCart shoppingCart1 = shoppingCartService.findById(shoppingCart.getId());
        shoppingCart1.setShippingMethod(shoppingCart.getShippingMethod());
        shoppingCart1.setPaymentMethod(shoppingCart.getPaymentMethod());
        shoppingCart1.setDescription(shoppingCart.getDescription());
        shoppingCart1.setShippingAddress(shippingAddress);

        shoppingCartService.save(shoppingCart1);

        //set shopping cart
        model.addAttribute("shoppingCart", shoppingCart1);

        //----------- validation ---------//
        if (result.hasErrors()) {
            return "/client/checkout";
        }

        if (shoppingCart1 == null || shoppingCart1.getCartItemList() == null) {
            //return "/client/checkout?cartEmpty";
            return "redirect:/checkout?cartEmpty";
        }
        //----------- end validation ---------//

        Customer customer = shoppingCart1.getCustomer();

        //save customer information
        customerService.save(customer);

//        String appUrl = "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();

        //Send email
//        emailService.orderCreation(appUrl, newOrder);

        if (shoppingCart.getPaymentMethod().equals("VNPay")) {
            String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
            String vnp_IpAddr = request.getRemoteAddr();
            String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
            vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(shoppingCart1.getGrandTotal().intValue() * 100));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", customer.getId().toString() + "," + shoppingCart.getId().toString());
            vnp_Params.put("vnp_OrderType", "order");
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_Returnurl);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            cld.add(Calendar.MINUTE, 30);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            List fieldNames = new ArrayList(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    //Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            String queryUrl = query.toString();
            String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = VNPayConfig.vnp_Url + "?" + queryUrl;
            response.sendRedirect(paymentUrl);
            return null;
        } else {
            Order newOrder = orderService.saveOrder(shoppingCart1);
            shoppingCartService.emptyShoppingCart(customer);

            return "redirect:/order-history?success";
        }
    }


    @GetMapping("/checkout/vnpay")
    public String getPaymentInfo(HttpServletResponse response, @ModelAttribute PaymentInfoDTO paymentInfoDTO) throws IOException {
        String[] info = paymentInfoDTO.getVnp_OrderInfo().split(",");
        Customer customer = customerService.findById(Long.valueOf(info[0]));
        ShoppingCart shoppingCart = shoppingCartService.findById(Long.valueOf(info[1]));
        shoppingCartService.emptyShoppingCart(customer);
        orderService.saveOrder(shoppingCart);

        return "redirect:/order-history?success";
    }
}
