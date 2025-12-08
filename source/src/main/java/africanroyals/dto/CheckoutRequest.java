package africanroyals.dto;

import africanroyals.model.CartCheckout.ShippingOption;

/**
 * Request body for performing a mock checkout.
 * Payment here is just "pretend" fields – no real processing.
 */
public class CheckoutRequest {

    private Long userId;
    private ShippingOption shippingOption;

    // Mock payment fields (no real processing, just for realism)
    private String cardHolderName;
    private String cardLastFourDigits;

    public CheckoutRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ShippingOption getShippingOption() {
        return shippingOption;
    }

    public void setShippingOption(ShippingOption shippingOption) {
        this.shippingOption = shippingOption;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardLastFourDigits() {
        return cardLastFourDigits;
    }

    public void setCardLastFourDigits(String cardLastFourDigits) {
        this.cardLastFourDigits = cardLastFourDigits;
    }
}

