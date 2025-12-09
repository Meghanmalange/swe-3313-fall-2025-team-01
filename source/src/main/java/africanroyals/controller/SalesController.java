package africanroyals.controller;

import africanroyals.entity.Sale;
import africanroyals.model.CartCheckout.Receipt;
import africanroyals.service.sales.SalesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/sales")
public class SalesController {

    private final SalesService salesService;

    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    /**
     * GET /api/sales/{id} - View receipt for a specific sale
     */
    @GetMapping("/{id}")
    public ResponseEntity<Receipt> viewReceipt(@PathVariable Long id) {
        Optional<Sale> saleOpt = salesService.getSaleById(id);
        
        if (saleOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Sale sale = saleOpt.get();
        Receipt receipt = salesService.generateReceiptFromSale(sale);
        
        return ResponseEntity.ok(receipt);
    }

    /**
     * POST /api/sales/record - Record a sale from a checkout receipt
     * This would typically be called by the checkout service
     */
    @PostMapping("/record")
    public ResponseEntity<Sale> recordSale(@RequestBody Receipt receipt) {
        try {
            Sale savedSale = salesService.recordSale(receipt);
            return ResponseEntity.ok(savedSale);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}