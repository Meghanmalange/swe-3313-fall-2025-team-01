package africanroyals.controller;

import africanroyals.dto.AddToCartRequest;
import africanroyals.model.CartCheckout.Cart;
import africanroyals.service.cart.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Get the current cart for a user.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId) {
        Cart cart = cartService.getCartForUser(userId);
        return ResponseEntity.ok(cart);
    }

    /**
     * Add a unique item to the user's cart.
     * If the item is already in the cart, it will simply not be added again.
     */
    @PostMapping("/items")
    public ResponseEntity<Cart> addItemToCart(@RequestBody AddToCartRequest request) {
        Cart cart = cartService.addItemToCart(
                request.getUserId(),
                request.getItemId(),
                request.getName(),
                request.getUnitPrice()
        );
        return ResponseEntity.ok(cart);
    }

    /**
     * Remove a single item from the cart.
     */
    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<Cart> removeItemFromCart(@PathVariable Long userId,
                                                   @PathVariable Long itemId) {
        Cart cart = cartService.removeItemFromCart(userId, itemId);
        return ResponseEntity.ok(cart);
    }

    /**
     * Clear the user's entire cart.
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Cart> clearCart(@PathVariable Long userId) {
        Cart cart = cartService.clearCart(userId);
        return ResponseEntity.ok(cart);
    }
}

