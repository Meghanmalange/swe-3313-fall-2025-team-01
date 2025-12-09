package africanroyals.controller;

import africanroyals.model.CartCheckout.Cart;
import africanroyals.model.Sale;
import africanroyals.repository.SaleRepository;
import africanroyals.service.cart.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;

@Controller
public class CheckoutPageController {

    private final CartService cartService;
    private final SaleRepository saleRepository;

    public CheckoutPageController(CartService cartService,
                                  SaleRepository saleRepository) {
        this.cartService = cartService;
        this.saleRepository = saleRepository;
    }

    // ---------- SHOW CHECKOUT PAGE (HTML) ----------
    @GetMapping("/checkout")
    public String showCheckout(Model model, HttpSession session) {

        // get logged-in user id from session
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        Cart cart = cartService.getCartForUser(userId);

        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            model.addAttribute("cartProducts", Collections.emptyList());
            model.addAttribute("subtotal", 0);
            model.addAttribute("tax", 0.0);
            model.addAttribute("totalBeforeShipping", 0.0);
            return "checkout"; // Thymeleaf "no items" step will show
        }

        int subtotal = 0;
        for (var item : cart.getItems()) {
            // adjust this if your CartItem has quantity
            subtotal += item.getUnitPrice();
        }

        double tax = subtotal * 0.06;
        double totalBeforeShipping = subtotal + tax;

        model.addAttribute("cartProducts", cart.getItems());
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("tax", tax);
        model.addAttribute("totalBeforeShipping", totalBeforeShipping);

        return "checkout"; // checkout.html
    }

    // ---------- COMPLETE CHECKOUT (HTML form submit) ----------
    @PostMapping("/checkout/complete")
    public String completeCheckout(@RequestParam String fullName,
                                   HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        Cart cart = cartService.getCartForUser(userId);
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            return "redirect:/catalog";
        }

        LocalDateTime now = LocalDateTime.now();

        // For each item in the cart, create a Sale record
        cart.getItems().forEach(item -> {
            Sale sale = new Sale();
            // adjust getters to match your CartItem class
            sale.setItemName(item.getName());
            sale.setBuyerName(fullName);
            sale.setDateSold(now);
            saleRepository.save(sale);
        });

        // clear the cart after successful purchase
        cartService.clearCart(userId);

        return "redirect:/checkout/confirmation";
    }

    // ---------- SIMPLE CONFIRMATION HTML PAGE ----------
    @GetMapping("/checkout/confirmation")
    public String confirmationPage() {
        return "checkout-confirmation"; // make checkout-confirmation.html
    }

    // ---------- /cart/remove FOR THYMELEAF ----------
    // Called by <a th:href="@{/cart/remove(productId=${item.itemId})}">
    @GetMapping("/cart/remove")
    public String removeFromCart(@RequestParam("productId") Long itemId,
                                 HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        cartService.removeItemFromCart(userId, itemId);

        return "redirect:/checkout";
    }
}
