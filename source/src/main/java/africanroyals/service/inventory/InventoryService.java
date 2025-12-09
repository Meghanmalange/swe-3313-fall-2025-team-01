package com.africanroyals.service.inventory;

import com.africanroyals.entity.InventoryItem;
import java.util.List;

public interface InventoryService {
    List<InventoryItem> getAvailableItems();
    List<InventoryItem> searchItems(String query);
}
