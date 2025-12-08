package africanroyals.service.sales;

import africanroyals.entity.Sale;
import africanroyals.model.CartCheckout.Receipt;

import java.util.List;
import java.util.Optional;

public interface SalesService {
    /**
     * Create a sale from a receipt after checkout
     */
    Sale createSaleFromReceipt(Receipt receipt);

    /**
     * Get all sales
     */
    List<Sale> getAllSales();

    /**
     * Get sale by ID
     */
    Optional<Sale> getSaleById(Long saleId);

    /**
     * Get all sales for a user
     */
    List<Sale> getSalesByUserId(Long userId);
}
