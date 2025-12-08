package africanroyals.controller;

import africanroyals.entity.InventoryItem;
import africanroyals.repository.InventoryItemRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryItemRepository inventoryItemRepository;

    public InventoryController(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    /**
     * Get all inventory items
     */
    @GetMapping
    public ResponseEntity<List<InventoryItem>> getAllInventoryItems() {
        List<InventoryItem> items = inventoryItemRepository.findAll();
        return ResponseEntity.ok(items);
    }

    /**
     * Get inventory item by ID
     */
    @GetMapping("/{itemId}")
    public ResponseEntity<InventoryItem> getInventoryItemById(@PathVariable Long itemId) {
        return inventoryItemRepository.findById(itemId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get available (not sold) inventory items
     */
    @GetMapping("/available")
    public ResponseEntity<List<InventoryItem>> getAvailableItems() {
        List<InventoryItem> items = inventoryItemRepository.findByIsSold(false);
        return ResponseEntity.ok(items);
    }
}
