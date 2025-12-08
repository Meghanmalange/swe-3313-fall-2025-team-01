package africanroyals.service.cart;

import africanroyals.model.CartCheckout.Cart;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CartServiceImplTest {

    private final CartServiceImpl cartService = new CartServiceImpl();

    @Test
    void addItemToCart_WhenItemNotInCart_AddsItemsAndUpdatesSubtotal() {
        // Arrange
        Long userId = 1L;

        // Act
        Cart cart = cartService.addItemToCart(userId, 101L, "Gold Lion Pendant", new BigDecimal("199.99"));
        cart = cartService.addItemToCart(userId, 102L, "Emerald Ring", new BigDecimal("150.00"));

        // Assert
        assertEquals(2, cart.getItems().size(), "Cart should contain two different items");
        assertEquals(new BigDecimal("349.99"), cart.getSubtotal(), "Subtotal should be sum of both items");
    }

    @Test
    void addItemToCart_WhenItemAlreadyInCart_DoesNotDuplicateOrChangeSubtotal() {
        // Arrange
        Long userId = 2L;

        // Act
        Cart cart = cartService.addItemToCart(userId, 201L, "Diamond Choker", new BigDecimal("300.00"));
        cart = cartService.addItemToCart(userId, 201L, "Diamond Choker", new BigDecimal("300.00")); // duplicate

        // Assert
        assertEquals(1, cart.getItems().size(), "Cart should still contain only one item");
        assertEquals(new BigDecimal("300.00"), cart.getSubtotal(), "Subtotal should stay the same on duplicate add");
    }
}
