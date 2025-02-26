package tshirt.ecommerce.client.controller;


import tshirt.ecommerce.library.model.Brand;
import tshirt.ecommerce.library.model.Category;
import tshirt.ecommerce.library.model.Product;
import tshirt.ecommerce.library.service.CategoryService;
import tshirt.ecommerce.library.service.BrandService;
import tshirt.ecommerce.library.service.ProductService;
import tshirt.ecommerce.library.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class PartController {
    @Autowired
    CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private Utility utility;

    private final Integer PAGE_SIZE=4;

    @RequestMapping("/category")
    public String category(@RequestParam("id") Optional<Long> id
            , Model model
            , @RequestParam("page") Optional<Integer> page
            , @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(PAGE_SIZE);

        model.addAttribute("classActiveCategory", "home active ");

        //Get all categories
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categories", categoryList);

        //Get selected product & category
        Page<Product> productList;
        Category category;
        if (id.isPresent()) {
            productList = productService.searchResults("", id.get().toString(), "1", PageRequest.of(currentPage-1, pageSize));
            //productList = productService.findAllByCategoryId(id.get(), PageRequest.of(currentPage-1, pageSize));
            category = categoryService.get(id.get());
        } else {
            productList = productService.searchResults("", "", "1", PageRequest.of(currentPage-1, pageSize));
            //productList = productService.findPaginated("", PageRequest.of(currentPage-1, pageSize));
            category = new Category();
            category.setName("Danh mục sản phẩm");
        }


        model.addAttribute("currentPage", currentPage);

        int totalPages = productList.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }



        model.addAttribute("productList", productList);
        model.addAttribute("category", category);

        return "/client/category";
    }

    @RequestMapping(value = "/part-search", method = RequestMethod.GET)
    public String partSearch(Model model
            , @RequestParam("name") Optional<String> name
            , @RequestParam("brand") Optional<String> brand
            , @RequestParam("category") Optional<String> category
            , @RequestParam("page") Optional<Integer> page
            , @RequestParam("size") Optional<Integer> size) {

        String keyword="",brandId="", categoryId="";

        if(name.isPresent()){
            keyword = name.get();
        }
        if(brand.isPresent()){
            brandId = brand.get();
        }
        if(category.isPresent()){
            categoryId=category.get();
        }
        model.addAttribute("name", keyword);
        model.addAttribute("brand", brandId);
        model.addAttribute("category", categoryId);

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(PAGE_SIZE);


        model.addAttribute("classActivePartSearch", "home active ");

        //All categories
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categories", categoryList);

        List<Brand> listBrand = brandService.findAll();
        model.addAttribute("listBrand", listBrand);

        List<Integer> listYear = utility.getYears();
        model.addAttribute("listYear", listYear);

        //All parts
        Page<Product> productList = productService.searchResults(keyword, categoryId, brandId, PageRequest.of(currentPage-1, pageSize));
        model.addAttribute("productList", productList);


        model.addAttribute("currentPage", currentPage);

        int totalPages = productList.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }


        return "/client/part-search";
    }

    //    //public String partSearch(@RequestParam("search") Optional<String> search, @RequestParam("category_id") Optional<String> categoryId, Model model) {

    /*@RequestMapping(value = "/part-search", method = RequestMethod.POST)
    public String partSearch2(
            @ModelAttribute("product") Product product
            , @RequestParam("page") Optional<Integer> page
            , @RequestParam("size") Optional<Integer> size
            , Model model) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(PAGE_SIZE);


        model.addAttribute("classActivePartSearch", "home active ");

        //All categories
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categories", categoryList);

        model.addAttribute("search", product);

        List<Make> listMake = makeService.findAll();
        model.addAttribute("listMake", listMake);

        List<nazeem.autoparts.library.model.Model> listBrand = modelService.getModels(listMake.get(0).getId());

        if(product.getMake() != null) {
            listBrand = modelService.getModels(product.getMake().getId());
        }
        model.addAttribute("listBrand", listBrand);

        List<Integer> listYear = utility.getYears();
        model.addAttribute("listYear", listYear);

        String categoryId="";
        if(product.getCategory()!=null){
            categoryId = product.getCategory().getId().toString();
        }

        String makeId="1";
        if(product.getMake()!=null){
            makeId = product.getMake().getId().toString();
        }

        String modelId="1";
        if(product.getModel()!=null){
            modelId = product.getModel().getId().toString();
        }

        String year="";
        if(product.getYear()!=null){
            year = product.getYear();
        }


        //All parts
        Page<Product> productList = productService.searchResults(
                product.getName()
                , categoryId
                , makeId
                , modelId
                , year
                , PageRequest.of(currentPage-1, pageSize));

        model.addAttribute("productList", productList);

        model.addAttribute("currentPage", currentPage);

        int totalPages = productList.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }


        return "/client/part-search";
    }*/

    @RequestMapping("/part-details")
    public String partDetails(@RequestParam("id") Long id, Model model) {
        model.addAttribute("classActivePartSearch", "home active ");
        model.addAttribute("search", new Product());

        //All categories
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categories", categoryList);

        List<Brand> listBrand = brandService.getBrands();
        model.addAttribute("listBrand", listBrand);

        List<Integer> listYear = utility.getYears();
        model.addAttribute("listYear", listYear);

        try {
            //Get product
            Product product = productService.get(id);
            model.addAttribute("product", product);
        }catch (Exception ex){
            model.addAttribute("error", ex.getMessage());
            return "/client/part-details";
        }
        return "/client/part-details";
    }


}
