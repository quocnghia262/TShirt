package tshirt.ecommerce.admin.controller;


import org.springframework.ui.Model;
import tshirt.ecommerce.library.model.Brand;
import tshirt.ecommerce.library.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    private String add_edit_template="/brand/add-edit-brand";
    private String list_template="/brand/list-brand";
    private String list_redirect="redirect:/brand/list";

    @GetMapping("/add")
    public String addModel(Brand brand2, Model model){
        model.addAttribute("model", brand2);

        return add_edit_template;
    }

    @GetMapping("/edit/{id}")
    public String editModel(@PathVariable("id") int id, org.springframework.ui.Model model){
        Brand brand2 = brandService.get(id);
        model.addAttribute("model", brand2);

        return add_edit_template;
    }

    @PostMapping("/save")
    public String saveModel(@Valid @ModelAttribute("brand") Brand brand2, BindingResult result, org.springframework.ui.Model model){
        model.addAttribute("brand", brand2);

        if(result.hasErrors()){
            return add_edit_template;
        }
        brandService.save(brand2);

        return list_redirect+"?success";
    }

    @GetMapping("/list")
    public String listBrand(org.springframework.ui.Model model) {
        List<Brand> listBrand = brandService.findAll();
        model.addAttribute("listBrand", listBrand);

        return list_template;
    }
}