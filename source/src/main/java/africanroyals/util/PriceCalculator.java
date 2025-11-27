package africanroyals.util;

import africanroyals.model.CartCheckout.Cart;
import africanroyals.model.CartCheckout.PriceDetails;
import africanroyals.model.CartCheckout.ShippingOption;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utility / helper bean for calculating subtotal, tax, shipping,
 * and grand total for a cart.
 */
@Component
public class PriceCalculator {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.06"); // 6%

    public PriceDetails calculate(Cart cart, ShippingOption shippingOption) {
        if (cart == null) {
            throw new IllegalArgumentException("Cart cannot be null");
        }
        if (shippingOption == null) {
            throw new IllegalArgumentException("Shipping option cannot be null");
        }

        BigDecimal subtotal = cart.getSubtotal();
        BigDecimal tax = subtotal
                .multiply(TAX_RATE)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal shippingFee = shippingOption.getFee();
        BigDecimal grandTotal = subtotal.add(tax).add(shippingFee);

        return new PriceDetails(subtotal, tax, shippingFee, grandTotal);
    }
}

