package africanroyals.model.CartCheckout;

import java.math.BigDecimal;

public class CartItem {

    private Long itemId;        // id of the jewelry item (from inventory)
    private String name;        // item name for display
    private BigDecimal unitPrice;

    public CartItem() {
    }

    public CartItem(Long itemId, String name, BigDecimal unitPrice) {
        this.itemId = itemId;
        this.name = name;
        this.unitPrice = unitPrice;
    }

    public BigDecimal getLineTotal() {
        // each item is unique, quantity is always 1
        return unitPrice;
    }

    // Getters and setters

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
