package africanroyals.model.CartCheckout;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Checkout receipt DTO returned right after a successful mock payment.
 * This is NOT a database entity – Team 4 will handle real sales records.
 */
public class Receipt {

    private Long orderId;                // can be filled from SalesService later
    private Long userId;
    private List<CartItem> items;
    private PriceDetails priceDetails;
    private ShippingOption shippingOption;
    private String cardHolderName;
    private String cardLastFourDigits;
    private LocalDateTime createdAt;

    public Receipt() {
    }

    public Receipt(Long orderId,
                   Long userId,
                   List<CartItem> items,
                   PriceDetails priceDetails,
                   ShippingOption shippingOption,
                   String cardHolderName,
                   String cardLastFourDigits,
                   LocalDateTime createdAt) {
        this.orderId = orderId;
        this.userId = userId;
        this.items = items;
        this.priceDetails = priceDetails;
        this.shippingOption = shippingOption;
        this.cardHolderName = cardHolderName;
        this.cardLastFourDigits = cardLastFourDigits;
        this.createdAt = createdAt;
    }

    // Getters and setters

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public PriceDetails getPriceDetails() {
        return priceDetails;
    }

    public void setPriceDetails(PriceDetails priceDetails) {
        this.priceDetails = priceDetails;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
