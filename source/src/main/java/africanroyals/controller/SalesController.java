package africanroyals.controller;

import africanroyals.entity.Sale;
import africanroyals.service.sales.SalesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sales")
public class SalesController {

    private final SalesService salesService;

    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    /**
     * Get all sales
     */
    @GetMapping
    public ResponseEntity<?> getAllSales() {
        try {
            List<Sale> sales = salesService.getAllSales();
            return ResponseEntity.ok(sales);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error loading sales: " + e.getMessage());
        }
    }

    /**
     * Get sale by ID
     */
    @GetMapping("/{saleId}")
    public ResponseEntity<?> getSaleById(@PathVariable Long saleId) {
        try {
            Optional<Sale> sale = salesService.getSaleById(saleId);
            return sale.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error loading sale: " + e.getMessage());
        }
    }

    /**
     * Get all sales for a user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getSalesByUserId(@PathVariable Long userId) {
        try {
            List<Sale> sales = salesService.getSalesByUserId(userId);
            return ResponseEntity.ok(sales);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error loading sales: " + e.getMessage());
        }
    }
}

