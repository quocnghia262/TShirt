package tshirt.ecommerce.admin.controller;


import tshirt.ecommerce.library.model.*;
import tshirt.ecommerce.library.service.CategoryService;
import tshirt.ecommerce.library.service.BrandService;
import tshirt.ecommerce.library.service.ProductService;
import tshirt.ecommerce.library.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private Utility utility;


    private String add_edit_template="/product/add-edit-product";
    private String list_template="/product/list-product";
    private String list_redirect="redirect:/product/list";


    @GetMapping("/add")
    public String addProduct(Product product, org.springframework.ui.Model model){
        model.addAttribute("product", product);
        List<Category> categories = categoryService.findAll().stream().filter(x->x.getIsActive() && !x.getIsDeleted()).collect(Collectors.toList());
        model.addAttribute("categories", categories);

        List<Brand> listBrand = brandService.getBrands();
        model.addAttribute("listBrand", listBrand);

        return add_edit_template;
    }


    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") long id, org.springframework.ui.Model model){
        Product product = productService.get(id);
        model.addAttribute("product", product);

        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);

        //get models if make is not empty
        List<Brand> listBrand = brandService.findAll();
        model.addAttribute("listBrand", listBrand);


        List<Integer> listYear = utility.getYears();
        model.addAttribute("listYear", listYear);

        return add_edit_template;
    }

    @PostMapping("/save")
    public String saveProduct(@Valid @ModelAttribute("product") Product product, BindingResult result, org.springframework.ui.Model model){
        model.addAttribute("product", product);
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);

        List<Integer> listYear = utility.getYears();
        model.addAttribute("listYear", listYear);

        if(result.hasErrors()){
            return add_edit_template;
        }
//        if(product.getId() == null){
//            model.addAttribute("error", "Mã áo phông không được trùng lặp");
//            return add_edit_template;
//        }

        if(product.getId() == null && productService.checkIfExistName(product.getName())){
            model.addAttribute("error", "Tên áo phông không được trùng lặp");
            return add_edit_template;
        }

        productService.save(product);
        return list_redirect+"?success";
    }



    /*@GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") long id, org.springframework.ui.Model model) {
        productService.delete(id);

        return list_redirect+"?deleted";
    }*/

    @GetMapping("/list")
    public String listProduct(org.springframework.ui.Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);

        List<Product> listProducts = productService.findAll();
        model.addAttribute("listProducts", listProducts);

        List<Brand> brandList = brandService.getBrands();
        model.addAttribute("brands", brandList);

        return list_template;
    }

    @RequestMapping(value = "/brands")
    @ResponseBody
    public List<Dropdown> getModels() {
        List<Brand> brandList = brandService.getBrands();
        List<Dropdown> dropdownList=new ArrayList<>();
        for (Brand brand : brandList) {
            dropdownList.add(new Dropdown(brand.getId(), brand.getName()));
        }
        return dropdownList;
    }

}