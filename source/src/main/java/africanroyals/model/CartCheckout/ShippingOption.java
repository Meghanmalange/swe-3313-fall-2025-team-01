package africanroyals.model.CartCheckout;

import java.math.BigDecimal;

public enum ShippingOption {

    OVERNIGHT(new BigDecimal("29.00")),
    THREE_DAY(new BigDecimal("19.00")),
    GROUND(BigDecimal.ZERO);

    private final BigDecimal fee;

    ShippingOption(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getFee() {
        return fee;
    }
}
