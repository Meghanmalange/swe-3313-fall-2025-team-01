package africanroyals.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "sale_items")
public class SaleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;

    @Column(name = "inventory_item_id", nullable = false)
    private Long inventoryItemId;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "unit_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    public SaleItem() {}

    public SaleItem(Sale sale, Long inventoryItemId, String itemName, BigDecimal unitPrice, int quantity) {
        this.sale = sale;
        this.inventoryItemId = inventoryItemId;
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public BigDecimal getLineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public Long getInventoryItemId() {
        return inventoryItemId;
    }

    public void setInventoryItemId(Long inventoryItemId) {
        this.inventoryItemId = inventoryItemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}