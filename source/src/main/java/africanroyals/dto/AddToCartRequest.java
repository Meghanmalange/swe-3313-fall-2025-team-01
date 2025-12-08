package africanroyals.dto;

import java.math.BigDecimal;

/**
 * Request body for adding a unique item to a user's cart.
 */
public class AddToCartRequest {

    private Long userId;
    private Long itemId;
    private String name;
    private BigDecimal unitPrice;

    public AddToCartRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
