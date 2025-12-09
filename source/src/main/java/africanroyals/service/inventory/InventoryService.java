package africanroyals.service.inventory;

import africanroyals.entity.InventoryItem;

import java.util.List;
import java.util.Optional;

public interface InventoryService {
    
    /**
     * Reduces the quantity of an inventory item by the specified amount
     * @param itemId The ID of the inventory item
     * @param quantity The quantity to reduce
     * @return true if successful, false if insufficient inventory
     */
    boolean reduceInventory(Long itemId, int quantity);
    
    /**
     * Gets an inventory item by ID
     * @param itemId The ID of the inventory item
     * @return Optional containing the item if found
     */
    Optional<InventoryItem> getInventoryItem(Long itemId);
    
    /**
     * Gets all inventory items
     * @return List of all inventory items
     */
    List<InventoryItem> getAllInventoryItems();
}