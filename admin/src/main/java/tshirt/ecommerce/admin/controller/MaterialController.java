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
import tshirt.ecommerce.library.model.Material;
import tshirt.ecommerce.library.service.MaterialService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/material")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    private String add_edit_template = "/material/add-edit-material";
    private String list_template = "/material/list-material";
    private String list_redirect = "redirect:/material/list";

    @GetMapping("/add")
    public String addMaterial(Material material, Model model) {
        model.addAttribute("material", material);
        return add_edit_template;
    }

    @GetMapping("/edit/{id}")
    public String editMaterial(@PathVariable("id") int id, Model model) {
        Material material = materialService.get(id);

        model.addAttribute("material", material);

        return add_edit_template;
    }

    @PostMapping("/save")
    public String saveMaterial(@Valid @ModelAttribute("material") Material material, BindingResult result, Model model) {
        model.addAttribute("material", material);

        if (result.hasErrors()) {
            return add_edit_template;
        }
        materialService.save(material);

        return list_redirect + "?success";
    }

    @GetMapping("/list")
    public String listMaterial(Model model) {
        List<Material> listMaterials = materialService.findAll();
        model.addAttribute("listMaterials", listMaterials);

        return list_template;
    }

}
