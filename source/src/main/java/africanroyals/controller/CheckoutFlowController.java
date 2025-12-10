package africanroyals.controller;

import africanroyals.model.CartCheckout.Cart;
import africanroyals.model.CartCheckout.Receipt;
import africanroyals.model.CartCheckout.ShippingOption;
import africanroyals.repository.UserRepository;
import africanroyals.service.cart.CartService;
import africanroyals.service.sales.SalesService;
import africanroyals.util.PriceCalculator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Controller
public class CheckoutFlowController {

    private final CartService cartService;
    private final PriceCalculator priceCalculator;
    private final SalesService salesService;
    private final UserRepository userRepository;

    public CheckoutFlowController(CartService cartService,
                                  PriceCalculator priceCalculator,
                                  SalesService salesService,
                                  UserRepository userRepository) {
        this.cartService = cartService;
        this.priceCalculator = priceCalculator;
        this.salesService = salesService;
        this.userRepository = userRepository;
    }

    @PostMapping("/checkout/complete")
    public String completeCheckout(@RequestParam(value = "shippingOption", defaultValue = "GROUND") String shippingOptionRaw,
                                   HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        Cart cart = cartService.getCartForUser(userId);
        if (cart.getItems().isEmpty()) {
            return "redirect:/checkout";
        }

        ShippingOption shippingOption;
        try {
            shippingOption = ShippingOption.valueOf(shippingOptionRaw);
        } catch (IllegalArgumentException ex) {
            shippingOption = ShippingOption.GROUND;
        }

        var priceDetails = priceCalculator.calculate(cart, shippingOption);

        Receipt receipt = new Receipt(
                null,
                userId,
                new ArrayList<>(cart.getItems()),
                priceDetails,
                shippingOption,
                LocalDateTime.now()
        );

        // Persist sale/order
        salesService.createSaleFromReceipt(receipt);

        // Clear the cart for this user
        cartService.clearCart(userId);

        return "redirect:/complete-purchase";
    }
}

