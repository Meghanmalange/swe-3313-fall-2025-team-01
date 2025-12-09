package africanroyals.service.sales;

import africanroyals.entity.Sale;
import africanroyals.model.CartCheckout.Receipt;

import java.util.Optional;

public interface SalesService {
    
    /**
     * Records a sale from a checkout receipt and updates inventory
     * @param receipt The checkout receipt to record
     * @return The saved Sale entity with generated ID
     */
    Sale recordSale(Receipt receipt);
    
    /**
     * Retrieves a sale by ID for viewing receipt
     * @param saleId The ID of the sale
     * @return Optional containing the sale if found
     */
    Optional<Sale> getSaleById(Long saleId);
    
    /**
     * Generates a receipt summary from a saved sale
     * @param sale The sale entity
     * @return Receipt object for display
     */
    Receipt generateReceiptFromSale(Sale sale);
}