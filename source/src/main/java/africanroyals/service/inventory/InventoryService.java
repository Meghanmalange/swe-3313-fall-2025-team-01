package africanroyals.service.inventory;

import africanroyals.entity.InventoryItem;

import java.util.List;

public interface InventoryService {
    List<InventoryItem> getAvailableItems();
    List<InventoryItem> searchItems(String query);
}
