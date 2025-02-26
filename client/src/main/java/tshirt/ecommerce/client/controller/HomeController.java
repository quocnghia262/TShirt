package tshirt.ecommerce.client.controller;


import tshirt.ecommerce.library.model.Brand;
import tshirt.ecommerce.library.model.Dropdown;
import tshirt.ecommerce.library.model.Product;
import tshirt.ecommerce.library.service.BrandService;
import tshirt.ecommerce.library.service.ProductService;
import tshirt.ecommerce.library.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private BrandService brandService;

    @Autowired
    private Utility utility;


    @Autowired
    ProductService productService;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("classActiveHome", "home active ");

        List<Brand> listBrand = brandService.findAll();
        model.addAttribute("listBrand", listBrand);

        List<Integer> listYear = utility.getYears();
        model.addAttribute("listYear", listYear);

        List<Product> productList = productService.findAllByActive();
        model.addAttribute("productList", productList);

        //Getting top 4
        List<Product> productList2 = productService.topMostOrderedProducts(8);
        model.addAttribute("productList2", productList2);

        return "/client/home";
    }



    @RequestMapping("/about-us")
    public String aboutUs(Model model) {
        model.addAttribute("classActivePages", "home active ");

        return "/client/about-us";
    }
    @RequestMapping("/contact-us")
    public String contactUs(Model model) {
        model.addAttribute("classActivePages", "home active ");

        return "/client/contact-us";
    }
    @RequestMapping("/faq")
    public String faq(Model model) {
        model.addAttribute("classActivePages", "home active ");

        return "/client/faq";
    }
    @RequestMapping("/privacy-policy")
    public String privacyPolicy(Model model) {
        model.addAttribute("classActivePages", "home active ");

        return "/client/privacy-policy";
    }
    @RequestMapping("/return-policy")
    public String returnPolicy(Model model) {
        model.addAttribute("classActivePages", "home active ");

        return "/client/return-policy";
    }
    @RequestMapping("/terms-and-conditions")
    public String termsAndConditions(Model model) {
        model.addAttribute("classActivePages", "home active ");

        return "/client/terms-and-conditions";
    }

    @RequestMapping(value = "/brands")
    @ResponseBody
    public List<Dropdown> getBrands() {
        List<Brand> brandList = brandService.findAll();
        List<Dropdown> dropdownList=new ArrayList<>();
        for (Brand brand : brandList) {
            dropdownList.add(new Dropdown(brand.getId(), brand.getName()));
        }
        return dropdownList;
    }

    // Added to test 500 page
    @RequestMapping(path = "/tigger-error", produces = MediaType.APPLICATION_JSON_VALUE)
    public void error500() throws Exception {
        throw new Exception();
    }

}
