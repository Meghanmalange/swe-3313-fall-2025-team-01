package africanroyals.controller;

import africanroyals.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@Controller
public class CatalogController {

    private final ProductService productService;

    // constructor injection
    public CatalogController(ProductService productService) {
        this.productService = productService;
    }

    // show catalog page for regular user
    @GetMapping("/catalog")
    public String catalogPage(Model model, HttpSession session) {

        // get cart from session (now using product IDs as Long)
        Set<Long> cart = (Set<Long>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashSet<>();
            session.setAttribute("cart", cart);
        }

        // send products + cart to the view
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("cart", cart);

        return "catalog";  // catalog.html
    }

    // add a product to the cart
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId, HttpSession session) {

        Set<Long> cart = (Set<Long>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashSet<>();
        }

        cart.add(productId); // store product ID (Long) in cart
        session.setAttribute("cart", cart);

        // send user back to catalog
        return "redirect:/catalog";
    }
}
