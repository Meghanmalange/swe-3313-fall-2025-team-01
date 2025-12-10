package africanroyals.service.checkout;

import africanroyals.dto.CheckoutRequest;
import africanroyals.model.CartCheckout.Cart;
import africanroyals.model.CartCheckout.CartItem;
import africanroyals.model.CartCheckout.PriceDetails;
import africanroyals.model.CartCheckout.ShippingOption;
import africanroyals.model.CartCheckout.Receipt;
import africanroyals.service.cart.CartService;
//import africanroyals.service.inventory.InventoryService;
//import africanroyals.service.sales.SalesService;
import africanroyals.util.PriceCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CheckoutServiceImplTest {

    @Mock
    private CartService cartService;

    @Mock
    private PriceCalculator priceCalculator;

    // These are here in case CheckoutServiceImpl depends on them.
    // We don’t need to stub them for this test.
    //@Mock
    //private SalesService salesService;

    //@Mock
    //private InventoryService inventoryService;

    @InjectMocks
    private CheckoutServiceImpl checkoutService;

    @Test
    void checkout_WhenCartHasItems_ReturnsReceiptWithTotals() {
        // Arrange
        Long userId = 1L;

        CheckoutRequest request = new CheckoutRequest();
        request.setUserId(userId);
        request.setShippingOption(ShippingOption.GROUND);
        request.setCardHolderName("Nyasha Muzerengi");
        request.setCardLastFourDigits("1234");

        Cart cart = new Cart(userId);
        cart.getItems().add(new CartItem(201L, "Diamond Choker", new BigDecimal("300.00")));

        when(cartService.getCartForUser(userId)).thenReturn(cart);

        PriceDetails priceDetails = new PriceDetails(
                new BigDecimal("300.00"),  // subtotal
                new BigDecimal("18.00"),   // tax (6%)
                BigDecimal.ZERO,           // shipping
                new BigDecimal("318.00")   // grand total
        );

        when(priceCalculator.calculate(cart, ShippingOption.GROUND)).thenReturn(priceDetails);

        // Act
        Receipt receipt = checkoutService.checkout(request);

        // Assert
        assertEquals(userId, receipt.getUserId());
        assertEquals(1, receipt.getItems().size(), "Receipt should contain one purchased item");
        assertEquals(priceDetails.getGrandTotal(), receipt.getPriceDetails().getGrandTotal(),
                "Grand total on receipt should match PriceDetails");
        assertEquals("1234", receipt.getCardLastFourDigits(), "Last four digits should be copied from request");
        assertEquals("Nyasha Muzerengi", receipt.getCardHolderName(), "Card holder name should be preserved");
        assertEquals(ShippingOption.GROUND, receipt.getShippingOption(), "Shipping option should match request");
    }
}
