package africanroyals.controller;

import africanroyals.entity.InventoryItem;
import africanroyals.repository.InventoryItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final InventoryItemRepository inventoryItemRepository;

    public AdminController(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    /**
     * Add a new inventory item
     */
    @PostMapping("/items")
    public ResponseEntity<?> addInventoryItem(@RequestBody InventoryItem item) {
        try {
            // Validate required fields
            if (item.getName() == null || item.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Item name is required");
            }
            if (item.getPrice() == null || item.getPrice() <= 0) {
                return ResponseEntity.badRequest().body("Valid price is required");
            }

            // Enforce uniqueness by name
            if (inventoryItemRepository.findByNameIgnoreCase(item.getName().trim()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Item already exists");
            }

            // Set default values
            if (item.getIsSold() == null) {
                item.setIsSold(false);
            }

            // Save the item
            InventoryItem savedItem = inventoryItemRepository.save(item);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to add item: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Form-based add item (from add-item.html)
     */
    @PostMapping("/add-item")
    public ResponseEntity<?> addInventoryItemForm(@RequestParam("name") String name,
                                                  @RequestParam("price") Double price,
                                                  @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Item name is required");
            }
            if (price == null || price <= 0) {
                return ResponseEntity.badRequest().body("Valid price is required");
            }
            if (inventoryItemRepository.findByNameIgnoreCase(name.trim()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Item already exists");
            }

            InventoryItem item = new InventoryItem();
            item.setName(name.trim());
            item.setPrice(price);
            item.setIsSold(false);
            if (imageFile != null && !imageFile.isEmpty()) {
                item.setImageUrl(imageFile.getOriginalFilename());
            }

            InventoryItem savedItem = inventoryItemRepository.save(item);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to add item: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Update an existing inventory item
     */
    @PutMapping("/items/{itemId}")
    public ResponseEntity<?> updateInventoryItem(@PathVariable Long itemId, @RequestBody InventoryItem item) {
        try {
            return inventoryItemRepository.findById(itemId)
                    .map(existingItem -> {
                        if (item.getName() != null) existingItem.setName(item.getName());
                        if (item.getDescription() != null) existingItem.setDescription(item.getDescription());
                        if (item.getPrice() != null) existingItem.setPrice(item.getPrice());
                        if (item.getImageUrl() != null) existingItem.setImageUrl(item.getImageUrl());
                        if (item.getIsSold() != null) existingItem.setIsSold(item.getIsSold());

                        InventoryItem updatedItem = inventoryItemRepository.save(existingItem);
                        return ResponseEntity.ok(updatedItem);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update item: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Delete an inventory item
     */
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<?> deleteInventoryItem(@PathVariable Long itemId) {
        try {
            if (!inventoryItemRepository.existsById(itemId)) {
                return ResponseEntity.notFound().build();
            }
            inventoryItemRepository.deleteById(itemId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Item deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to delete item: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}