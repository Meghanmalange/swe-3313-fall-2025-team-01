package africanroyals.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class PageController {

    @GetMapping({"/", "/index"})
    public String showIndex() {
        return "index";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/signup")
    public String showSignup() {
        return "signup";
    }

    @GetMapping("/catalog")
    public String showCatalog() {
        return "catalog";
    }

    @GetMapping("/admin-catalog")
    public String showAdminCatalog() {
        return "admin-catalog";
    }

    @GetMapping("/search")
    public String showSearch() {
        return "search";
    }

    @GetMapping("/checkout")
    public String showCheckout() {
        return "checkout";
    }

    @GetMapping("/complete-purchase")
    public String completePurchase() {
        return "complete-purchase";
    }

}
