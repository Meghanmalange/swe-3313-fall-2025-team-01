package africanroyals.service.sales;

import africanroyals.entity.Sale;
import africanroyals.entity.SaleItem;
import africanroyals.entity.InventoryItem;
import africanroyals.model.CartCheckout.CartItem;
import africanroyals.model.CartCheckout.Receipt;
import africanroyals.model.CartCheckout.PriceDetails;
import africanroyals.model.CartCheckout.ShippingOption;
import africanroyals.repository.SaleRepository;
import africanroyals.repository.SaleItemRepository;
import africanroyals.service.inventory.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class SalesServiceImplTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private SaleItemRepository saleItemRepository;

    @Mock
    private InventoryService inventoryService;

    private SalesServiceImpl salesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        salesService = new SalesServiceImpl(saleRepository, saleItemRepository, inventoryService);
    }

    @Test
    void testRecordSale() {
        // Arrange
        Long userId = 1L;
        CartItem cartItem1 = new CartItem(1L, "Royal Necklace", new BigDecimal("1200.00"));
        CartItem cartItem2 = new CartItem(2L, "Zulu Bracelet", new BigDecimal("80.00"));
        List<CartItem> cartItems = Arrays.asList(cartItem1, cartItem2);

        PriceDetails priceDetails = new PriceDetails(
            new BigDecimal("1280.00"), // subtotal
            new BigDecimal("128.00"),  // tax
            new BigDecimal("19.00"),   // shipping
            new BigDecimal("1427.00")  // grand total
        );

        Receipt receipt = new Receipt(
            null,
            userId,
            cartItems,
            priceDetails,
            ShippingOption.THREE_DAY,
            LocalDateTime.now()
        );

        Sale savedSale = new Sale(userId, 
            priceDetails.getSubtotal(),
            priceDetails.getTax(),
            priceDetails.getShippingFee(),
            priceDetails.getGrandTotal());
        savedSale.setId(1L);

        // Mock behavior
        when(saleRepository.save(any(Sale.class))).thenReturn(savedSale);
        when(inventoryService.reduceInventory(1L, 1)).thenReturn(true);
        when(inventoryService.reduceInventory(2L, 1)).thenReturn(true);
        when(saleItemRepository.saveAll(any(List.class))).thenReturn(Arrays.asList());

        // Act
        Sale result = salesService.recordSale(receipt);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(userId, result.getUserId());
        assertEquals(priceDetails.getGrandTotal(), result.getGrandTotal());

        // Verify interactions
        verify(saleRepository).save(any(Sale.class));
        verify(inventoryService).reduceInventory(1L, 1);
        verify(inventoryService).reduceInventory(2L, 1);
        verify(saleItemRepository).saveAll(any(List.class));
    }

    @Test
    void testRecordSaleInventoryFailure() {
        // Arrange
        Long userId = 1L;
        CartItem cartItem = new CartItem(1L, "Royal Necklace", new BigDecimal("1200.00"));
        List<CartItem> cartItems = Arrays.asList(cartItem);

        PriceDetails priceDetails = new PriceDetails(
            new BigDecimal("1200.00"),
            new BigDecimal("120.00"),
            new BigDecimal("19.00"),
            new BigDecimal("1339.00")
        );

        Receipt receipt = new Receipt(
            null,
            userId,
            cartItems,
            priceDetails,
            ShippingOption.THREE_DAY,
            LocalDateTime.now()
        );

        Sale savedSale = new Sale(userId, 
            priceDetails.getSubtotal(),
            priceDetails.getTax(),
            priceDetails.getShippingFee(),
            priceDetails.getGrandTotal());
        savedSale.setId(1L);

        // Mock behavior - inventory update fails
        when(saleRepository.save(any(Sale.class))).thenReturn(savedSale);
        when(inventoryService.reduceInventory(1L, 1)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            salesService.recordSale(receipt);
        });

        assertTrue(exception.getMessage().contains("Failed to update inventory"));
    }

    @Test
    void testGetSaleById() {
        // Arrange
        Long saleId = 1L;
        Sale sale = new Sale(1L, new BigDecimal("1200.00"), new BigDecimal("120.00"), 
                           new BigDecimal("19.00"), new BigDecimal("1339.00"));
        sale.setId(saleId);

        when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));

        // Act
        Optional<Sale> result = salesService.getSaleById(saleId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(saleId, result.get().getId());
        verify(saleRepository).findById(saleId);
    }

    @Test
    void testGenerateReceiptFromSale() {
        // Arrange
        Long saleId = 1L;
        Long userId = 1L;
        
        Sale sale = new Sale(userId, new BigDecimal("1200.00"), new BigDecimal("120.00"), 
                           new BigDecimal("19.00"), new BigDecimal("1339.00"));
        sale.setId(saleId);

        SaleItem saleItem = new SaleItem(sale, 1L, "Royal Necklace", new BigDecimal("1200.00"), 1);
        sale.setSaleItems(Arrays.asList(saleItem));

        // Act
        Receipt receipt = salesService.generateReceiptFromSale(sale);

        // Assert
        assertNotNull(receipt);
        assertEquals(saleId, receipt.getOrderId());
        assertEquals(userId, receipt.getUserId());
        assertEquals(1, receipt.getItems().size());
        assertEquals("Royal Necklace", receipt.getItems().get(0).getName());
        assertEquals(new BigDecimal("1339.00"), receipt.getPriceDetails().getGrandTotal());
        assertEquals(ShippingOption.THREE_DAY, receipt.getShippingOption());
    }
}
