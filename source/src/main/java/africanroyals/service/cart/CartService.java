package africanroyals.service.cart;

import africanroyals.model.CartCheckout.Cart;

import java.math.BigDecimal;

public interface CartService {

    /**
     * Returns the current cart for the user, creating a new one if needed.
     */
    Cart getCartForUser(Long userId);

    /**
     * Add a unique item to the user's cart.
     * If the item is already in the cart, this method can either:
     * - ignore the request, or
     * - throw an exception (we'll decide in the impl).
     */
    Cart addItemToCart(Long userId,
                       Long itemId,
                       String name,
                       BigDecimal unitPrice);

    /**
     * Remove an item completely from the user's cart.
     */
    Cart removeItemFromCart(Long userId, Long itemId);

    /**
     * Clear all items from the user's cart.
     */
    Cart clearCart(Long userId);
}
