package tshirt.ecommerce.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tshirt.ecommerce.library.model.Size;
import tshirt.ecommerce.library.service.SizeService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/size")
public class SizeController {

    @Autowired
    private SizeService sizeService;

    private String add_edit_template = "/size/add-edit-size";
    private String list_template = "/size/list-size";
    private String list_redirect = "redirect:/size/list";

    @GetMapping("/add")
    public String addSize(Size size, Model model) {
        model.addAttribute("size", size);
        return add_edit_template;
    }

    @GetMapping("/edit/{id}")
    public String editSize(@PathVariable("id") int id, Model model) {
        Size size = sizeService.get(id);

        model.addAttribute("size", size);

        return add_edit_template;
    }

    @PostMapping("/save")
    public String saveSize(@Valid @ModelAttribute("size") Size size, BindingResult result, Model model) {
        model.addAttribute("size", size);

        if (result.hasErrors()) {
            return add_edit_template;
        }
        sizeService.save(size);

        return list_redirect + "?success";
    }

    @GetMapping("/list")
    public String listSize(Model model) {
        List<Size> listSizes = sizeService.findAll();
        model.addAttribute("listSizes", listSizes);

        return list_template;
    }
    
}
