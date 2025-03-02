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
import tshirt.ecommerce.library.model.Color;
import tshirt.ecommerce.library.service.ColorService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/color")
public class ColorController {

    @Autowired
    private ColorService colorService;

    private String add_edit_template = "/color/add-edit-color";
    private String list_template = "/color/list-color";
    private String list_redirect = "redirect:/color/list";

    @GetMapping("/add")
    public String addColor(Color color, Model model) {
        model.addAttribute("color", color);
        return add_edit_template;
    }

    @GetMapping("/edit/{id}")
    public String editColor(@PathVariable("id") int id, Model model) {
        Color color = colorService.get(id);

        model.addAttribute("color", color);

        return add_edit_template;
    }

    @PostMapping("/save")
    public String saveColor(@Valid @ModelAttribute("color") Color color, BindingResult result, Model model) {
        model.addAttribute("color", color);

        if (result.hasErrors()) {
            return add_edit_template;
        }
        colorService.save(color);

        return list_redirect + "?success";
    }

    @GetMapping("/list")
    public String listColor(Model model) {
        List<Color> listColors = colorService.findAll();
        model.addAttribute("listColors", listColors);

        return list_template;
    }

}
