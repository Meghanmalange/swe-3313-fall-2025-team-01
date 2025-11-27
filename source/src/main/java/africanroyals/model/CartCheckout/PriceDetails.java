package africanroyals.model.CartCheckout;

import java.math.BigDecimal;

public class PriceDetails {

    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal shippingFee;
    private BigDecimal grandTotal;

    public PriceDetails() {
    }

    public PriceDetails(BigDecimal subtotal,
                        BigDecimal tax,
                        BigDecimal shippingFee,
                        BigDecimal grandTotal) {
        this.subtotal = subtotal;
        this.tax = tax;
        this.shippingFee = shippingFee;
        this.grandTotal = grandTotal;
    }

    // Getters and setters

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
}
