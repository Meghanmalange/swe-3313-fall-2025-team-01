package africanroyals.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "subtotal", precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;

    @Column(name = "tax", precision = 10, scale = 2, nullable = false)
    private BigDecimal tax;

    @Column(name = "shipping_fee", precision = 10, scale = 2, nullable = false)
    private BigDecimal shippingFee;

    @Column(name = "grand_total", precision = 10, scale = 2, nullable = false)
    private BigDecimal grandTotal;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SaleItem> saleItems;

    public Sale() {
        this.timestamp = LocalDateTime.now();
    }

    public Sale(Long userId, BigDecimal subtotal, BigDecimal tax, BigDecimal shippingFee, BigDecimal grandTotal) {
        this.userId = userId;
        this.subtotal = subtotal;
        this.tax = tax;
        this.shippingFee = shippingFee;
        this.grandTotal = grandTotal;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(BigDecimal shippingFee) {
        this.shippingFee = shippingFee;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<SaleItem> getSaleItems() {
        return saleItems;
    }

    public void setSaleItems(List<SaleItem> saleItems) {
        this.saleItems = saleItems;
    }
}