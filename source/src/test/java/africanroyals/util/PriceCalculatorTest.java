package africanroyals.util;

import africanroyals.model.CartCheckout.Cart;
import africanroyals.model.CartCheckout.CartItem;
import africanroyals.model.CartCheckout.PriceDetails;
import africanroyals.model.CartCheckout.ShippingOption;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PriceCalculatorTest {

    private final PriceCalculator priceCalculator = new PriceCalculator();

    @Test
    void calculate_WhenGroundShipping_ReturnsExpectedTotals() {
        // Arrange
        Cart cart = new Cart(1L);
        cart.getItems().add(new CartItem(201L, "Diamond Choker", new BigDecimal("300.00")));
        cart.getItems().add(new CartItem(202L, "Ruby Bracelet", new BigDecimal("125.50")));

        // Act
        PriceDetails result = priceCalculator.calculate(cart, ShippingOption.GROUND);

        // Assert
        assertEquals(new BigDecimal("425.50"), result.getSubtotal(), "Subtotal should match sum of items");
        assertEquals(new BigDecimal("25.53"), result.getTax(), "Tax should be 6% of subtotal");
        assertEquals(BigDecimal.ZERO, result.getShippingFee(), "Ground shipping is free");
        assertEquals(new BigDecimal("451.03"), result.getGrandTotal(), "Grand total should be subtotal + tax + shipping");
    }

    @Test
    void calculate_WhenOvernightShipping_IncludesShippingFee() {
        // Arrange
        Cart cart = new Cart(1L);
        cart.getItems().add(new CartItem(301L, "Gold Anklet", new BigDecimal("100.00")));

        // Act
        PriceDetails result = priceCalculator.calculate(cart, ShippingOption.OVERNIGHT);

        // Assert
        assertEquals(new BigDecimal("100.00"), result.getSubtotal());
        assertEquals(new BigDecimal("6.00"), result.getTax());          // 6% of 100
        assertEquals(new BigDecimal("29.00"), result.getShippingFee()); // Overnight shipping
        assertEquals(new BigDecimal("135.00"), result.getGrandTotal());
    }
}
