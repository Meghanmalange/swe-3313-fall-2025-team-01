package africanroyals.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("/checkout/complete")
    public String handleCheckoutComplete(
            @RequestParam("fullName") String fullName,
            Model model) {

        // TODO:
        // 1. Look up the current cart from session or your CartService
        //    e.g. List<CartItem> cartItems = cartService.getCartForCurrentUser();
        // 2. Compute subtotal, tax, total FROM BACKEND (not trusting the HTML)
        // 3. Save a Sale / Order record with:
        //      - buyer name (fullName)
        //      - items (cart items)
        //      - totals
        // 4. Clear the cart for this user

        // If you want, you can pass info to the confirmation page:
        // model.addAttribute("buyerName", fullName);
        // model.addAttribute("finalTotal", total);

        // Redirect to the thank-you page
        return "redirect:/complete-purchase";
    }

    @GetMapping("/complete-purchase")
    public String completePurchase() {
        return "complete-purchase";
    }

    @GetMapping("/add-item")
    public String showAddItem() {
        return "add-item";
    }

    @GetMapping("/sales-report")
    public String showSalesReport() {
        return "sales-report";
    }

    @GetMapping("/promote-user")
    public String showPromoteUser() {
        return "promote-user";
    }

}
