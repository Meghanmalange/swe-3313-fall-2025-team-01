package africanroyals.service.checkout;

import africanroyals.dto.CheckoutRequest;
import africanroyals.model.CartCheckout.Cart;
import africanroyals.model.CartCheckout.PriceDetails;
import africanroyals.model.CartCheckout.Receipt;
import africanroyals.service.cart.CartService;
import africanroyals.service.sales.SalesService;
import africanroyals.util.PriceCalculator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final CartService cartService;
    private final PriceCalculator priceCalculator;
    private final SalesService salesService;

    public CheckoutServiceImpl(CartService cartService,
                               PriceCalculator priceCalculator,
                               SalesService salesService) {
        this.cartService = cartService;
        this.priceCalculator = priceCalculator;
        this.salesService = salesService;
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
                null, // orderId will be set after sale creation
                userId,
                new ArrayList<>(cart.getItems()),
                priceDetails,
                request.getShippingOption(),
                LocalDateTime.now()
        );

        // 4. Create a sale from the receipt
        var sale = salesService.createSaleFromReceipt(receipt);
        receipt.setOrderId(sale.getId());

        // 5. Clear the cart after successful checkout (items are considered sold)
        cartService.clearCart(userId);

        return receipt;
    }
}

