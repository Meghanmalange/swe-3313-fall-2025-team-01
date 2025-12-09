package africanroyals.service.sales;

import africanroyals.entity.Sale;
import africanroyals.entity.SaleItem;
import africanroyals.model.CartCheckout.CartItem;
import africanroyals.model.CartCheckout.Receipt;
import africanroyals.model.CartCheckout.PriceDetails;
import africanroyals.model.CartCheckout.ShippingOption;
import africanroyals.repository.SaleRepository;
import africanroyals.repository.SaleItemRepository;
import africanroyals.service.inventory.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SalesServiceImpl implements SalesService {

    private final SaleRepository saleRepository;
    private final SaleItemRepository saleItemRepository;
    private final InventoryService inventoryService;

    public SalesServiceImpl(SaleRepository saleRepository, 
                           SaleItemRepository saleItemRepository,
                           InventoryService inventoryService) {
        this.saleRepository = saleRepository;
        this.saleItemRepository = saleItemRepository;
        this.inventoryService = inventoryService;
    }

    @Override
    @Transactional
    public Sale recordSale(Receipt receipt) {
        // 1. Create and save the Sale entity
        Sale sale = new Sale(
            receipt.getUserId(),
            receipt.getPriceDetails().getSubtotal(),
            receipt.getPriceDetails().getTax(),
            receipt.getPriceDetails().getShippingFee(),
            receipt.getPriceDetails().getGrandTotal()
        );
        
        sale = saleRepository.save(sale);

        // 2. Create SaleItem entries and update inventory
        List<SaleItem> saleItems = new ArrayList<>();
        
        for (CartItem cartItem : receipt.getItems()) {
            // Create sale item (quantity is always 1 for jewelry items)
            SaleItem saleItem = new SaleItem(
                sale,
                cartItem.getItemId(),
                cartItem.getName(),
                cartItem.getUnitPrice(),
                1 // Each jewelry item is unique, quantity is always 1
            );
            
            saleItems.add(saleItem);
            
            // Update inventory - reduce quantity by 1
            boolean inventoryUpdated = inventoryService.reduceInventory(cartItem.getItemId(), 1);
            
            if (!inventoryUpdated) {
                throw new RuntimeException("Failed to update inventory for item: " + cartItem.getName());
            }
        }
        
        // Save all sale items
        saleItemRepository.saveAll(saleItems);
        sale.setSaleItems(saleItems);
        
        return sale;
    }

    @Override
    public Optional<Sale> getSaleById(Long saleId) {
        return saleRepository.findById(saleId);
    }

    @Override
    public Receipt generateReceiptFromSale(Sale sale) {
        // Convert SaleItems back to CartItems
        List<CartItem> cartItems = new ArrayList<>();
        
        for (SaleItem saleItem : sale.getSaleItems()) {
            CartItem cartItem = new CartItem(
                saleItem.getInventoryItemId(),
                saleItem.getItemName(),
                saleItem.getUnitPrice()
            );
            cartItems.add(cartItem);
        }
        
        // Create PriceDetails
        PriceDetails priceDetails = new PriceDetails(
            sale.getSubtotal(),
            sale.getTax(),
            sale.getShippingFee(),
            sale.getGrandTotal()
        );
        
        // Create Receipt (we don't store shipping option, so we'll determine it from fee)
        ShippingOption shippingOption = determineShippingOptionFromFee(sale.getShippingFee());
        
        return new Receipt(
            sale.getId(),
            sale.getUserId(),
            cartItems,
            priceDetails,
            shippingOption,
            sale.getTimestamp()
        );
    }
    
    private ShippingOption determineShippingOptionFromFee(java.math.BigDecimal fee) {
        if (fee.compareTo(ShippingOption.OVERNIGHT.getFee()) == 0) {
            return ShippingOption.OVERNIGHT;
        } else if (fee.compareTo(ShippingOption.THREE_DAY.getFee()) == 0) {
            return ShippingOption.THREE_DAY;
        } else {
            return ShippingOption.GROUND; // Default to ground for zero or unknown fees
        }
    }
}