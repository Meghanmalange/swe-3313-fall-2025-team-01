package africanroyals.controller;

import africanroyals.dto.CheckoutRequest;
import africanroyals.model.CartCheckout.Receipt;
import africanroyals.service.checkout.CheckoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    /**
     * Perform a mock checkout for the user's current cart.
     * Returns a receipt-style summary.
     */
    @PostMapping
    public ResponseEntity<Receipt> checkout(@RequestBody CheckoutRequest request) {
        Receipt receipt = checkoutService.checkout(request);
        return ResponseEntity.ok(receipt);
    }
}

