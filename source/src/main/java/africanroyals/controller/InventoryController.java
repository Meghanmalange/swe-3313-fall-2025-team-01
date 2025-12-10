package africanroyals.controller;

import africanroyals.entity.InventoryItem;
import africanroyals.service.inventory.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<InventoryItem>> getInventory() {
        return ResponseEntity.ok(inventoryService.getAvailableItems());
    }

    @GetMapping("/search")
    public ResponseEntity<List<InventoryItem>> search(@RequestParam String query) {
        return ResponseEntity.ok(inventoryService.searchItems(query));
    }
}
