package africanroyals.service.checkout;

import africanroyals.dto.CheckoutRequest;
import africanroyals.model.CartCheckout.Receipt;

public interface CheckoutService {

    /**
     * Perform a mock checkout:
     * - Use the user's current cart
     * - Apply tax + shipping
     * - Pretend to take payment
     * - Return a receipt-like summary
     */
    Receipt checkout(CheckoutRequest request);
}

