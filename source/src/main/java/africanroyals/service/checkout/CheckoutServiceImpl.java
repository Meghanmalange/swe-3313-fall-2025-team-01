package africanroyals.service.checkout;

import africanroyals.dto.CheckoutRequest;
import africanroyals.model.CartCheckout.Cart;
import africanroyals.model.CartCheckout.PriceDetails;
import africanroyals.model.CartCheckout.Receipt;
import africanroyals.service.cart.CartService;
import africanroyals.util.PriceCalculator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final CartService cartService;
    private final PriceCalculator priceCalculator;

    public CheckoutServiceImpl(CartService cartService,
                               PriceCalculator priceCalculator) {
        this.cartService = cartService;
        this.priceCalculator = priceCalculator;
    }

    @Override
    public Receipt checkout(CheckoutRequest request) {
        Long userId = request.getUserId();

        // 1. Get the user's current cart
        Cart cart = cartService.getCartForUser(userId);

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot checkout with an empty cart.");
        }

        // 2. Calculate prices (subtotal, tax, shipping, grand total)
        PriceDetails priceDetails =
                priceCalculator.calculate(cart, request.getShippingOption());

        // 3. Build a receipt DTO to return
        Receipt receipt = new Receipt(
                null, // orderId will be plugged in by SalesService (Team 4) later
                userId,
                new ArrayList<>(cart.getItems()),
                priceDetails,
                request.getShippingOption(),
                request.getCardHolderName(),
                request.getCardLastFourDigits(),
                LocalDateTime.now()
        );

        // 4. Clear the cart after successful checkout (items are considered sold)
        cartService.clearCart(userId);

        return receipt;
    }
}

