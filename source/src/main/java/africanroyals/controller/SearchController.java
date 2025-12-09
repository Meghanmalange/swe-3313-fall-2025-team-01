package africanroyals.controller;

import africanroyals.model.Product;
import africanroyals.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {

    private final ProductService productService;

    public SearchController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String q, Model model) {

        if(q == null || q.isEmpty()){
            model.addAttribute("results", productService.getAllProducts());
            model.addAttribute("query", "");
            return "search";
        }

        List<Product> results = productService.searchByName(q);

        model.addAttribute("results", results);
        model.addAttribute("query", q);

        return "search"; // search.html
    }
}
