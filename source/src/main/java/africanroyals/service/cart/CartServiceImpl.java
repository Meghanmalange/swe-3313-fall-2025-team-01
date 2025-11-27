package africanroyals.service.cart;

import africanroyals.model.CartCheckout.Cart;
import africanroyals.model.CartCheckout.CartItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CartServiceImpl implements CartService {

    // Simple in-memory storage: one Cart per userId
    private final Map<Long, Cart> cartsByUser = new ConcurrentHashMap<>();

    @Override
    public Cart getCartForUser(Long userId) {
        return cartsByUser.computeIfAbsent(userId, Cart::new);
    }

    @Override
    public Cart addItemToCart(Long userId,
                              Long itemId,
                              String name,
                              BigDecimal unitPrice) {

        Cart cart = getCartForUser(userId);

        // Check if this unique item is already in the cart
        boolean alreadyInCart = cart.getItems().stream()
                .anyMatch(ci -> ci.getItemId().equals(itemId));

        if (!alreadyInCart) {
            CartItem newItem = new CartItem(itemId, name, unitPrice);
            cart.getItems().add(newItem);
        }
        // If it was already in the cart, we just ignore the request

        return cart;
    }

    @Override
    public Cart removeItemFromCart(Long userId, Long itemId) {
        Cart cart = getCartForUser(userId);
        cart.getItems().removeIf(ci -> ci.getItemId().equals(itemId));
        return cart;
    }

    @Override
    public Cart clearCart(Long userId) {
        Cart cart = getCartForUser(userId);
        cart.getItems().clear();
        return cart;
    }
}

