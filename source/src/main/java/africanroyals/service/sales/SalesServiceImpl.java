package africanroyals.service.sales;

import africanroyals.entity.InventoryItem;
import africanroyals.entity.Sale;
import africanroyals.entity.User;
import africanroyals.model.CartCheckout.CartItem;
import africanroyals.model.CartCheckout.PriceDetails;
import africanroyals.model.CartCheckout.Receipt;
import africanroyals.repository.InventoryItemRepository;
import africanroyals.repository.SaleRepository;
import africanroyals.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SalesServiceImpl implements SalesService {

    private final SaleRepository saleRepository;
    private final UserRepository userRepository;
    private final InventoryItemRepository inventoryItemRepository;

    public SalesServiceImpl(SaleRepository saleRepository,
                           UserRepository userRepository,
                           InventoryItemRepository inventoryItemRepository) {
        this.saleRepository = saleRepository;
        this.userRepository = userRepository;
        this.inventoryItemRepository = inventoryItemRepository;
    }

    @Override
    @Transactional
    public Sale createSaleFromReceipt(Receipt receipt) {
        User user = userRepository.findById(receipt.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + receipt.getUserId()));

        PriceDetails priceDetails = receipt.getPriceDetails();
        LocalDateTime saleDate = receipt.getCreatedAt() != null ? receipt.getCreatedAt() : LocalDateTime.now();

        Sale sale = new Sale();
        sale.setUser(user);
        sale.setSaleDate(saleDate);
        sale.setSubTotal(priceDetails.getSubtotal() != null ? priceDetails.getSubtotal().doubleValue() : null);
        sale.setTax(priceDetails.getTax() != null ? priceDetails.getTax().doubleValue() : null);
        sale.setShippingMethod(receipt.getShippingOption() != null ? receipt.getShippingOption().name() : null);
        sale.setShippingCost(priceDetails.getShippingFee() != null ? priceDetails.getShippingFee().doubleValue() : null);
        sale.setTotal(priceDetails.getGrandTotal() != null ? priceDetails.getGrandTotal().doubleValue() : 0.0);

        // Add inventory items to the sale and mark them as sold
        for (CartItem cartItem : receipt.getItems()) {
            InventoryItem inventoryItem = inventoryItemRepository.findById(cartItem.getItemId())
                    .orElseThrow(() -> new IllegalArgumentException("Inventory item not found: " + cartItem.getItemId()));

            sale.getItems().add(inventoryItem);

            // Mark inventory item as sold
            inventoryItem.setIsSold(true);
            inventoryItemRepository.save(inventoryItem);
        }

        return saleRepository.save(sale);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sale> getAllSales() {
        List<Sale> sales = saleRepository.findAll();
        // Force initialization of lazy-loaded collections
        sales.forEach(sale -> {
            if (sale.getItems() != null) {
                sale.getItems().size();
            }
            if (sale.getUser() != null) {
                sale.getUser().getId();
            }
        });
        return sales;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Sale> getSaleById(Long saleId) {
        Optional<Sale> sale = saleRepository.findById(saleId);
        sale.ifPresent(s -> {
            if (s.getItems() != null) {
                s.getItems().size(); // Initialize the inventory items list
            }
            if (s.getUser() != null) {
                s.getUser().getId();
            }
        });
        return sale;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sale> getSalesByUserId(Long userId) {
        List<Sale> sales = saleRepository.findByUserId(userId);
        // Force initialization of lazy-loaded collections
        sales.forEach(sale -> {
            if (sale.getItems() != null) {
                sale.getItems().size();
            }
            if (sale.getUser() != null) {
                sale.getUser().getId();
            }
        });
        return sales;
    }
    
    @Override
    @Transactional
    public Sale updateSaleShippingDetails(Long saleId, String address, String city, String state, String zip, String details) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new IllegalArgumentException("Sale not found: " + saleId));
        
        sale.setShippingAddress(address);
        sale.setShippingCity(city);
        sale.setShippingState(state);
        sale.setShippingZip(zip);
        sale.setShippingDetails(details != null ? details : "");
        
        return saleRepository.save(sale);
    }
}
