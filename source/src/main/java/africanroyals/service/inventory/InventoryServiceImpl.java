package africanroyals.service.inventory;

import africanroyals.entity.InventoryItem;
import africanroyals.repository.InventoryItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryItemRepository inventoryItemRepository;

    public InventoryServiceImpl(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    @Override
    @Transactional
    public boolean reduceInventory(Long itemId, int quantity) {
        Optional<InventoryItem> itemOpt = inventoryItemRepository.findById(itemId);
        
        if (itemOpt.isEmpty()) {
            return false;
        }
        
        InventoryItem item = itemOpt.get();
        
        if (item.getQuantity() < quantity) {
            return false; // Insufficient inventory
        }
        
        item.setQuantity(item.getQuantity() - quantity);
        inventoryItemRepository.save(item);
        
        return true;
    }

    @Override
    public Optional<InventoryItem> getInventoryItem(Long itemId) {
        return inventoryItemRepository.findById(itemId);
    }

    @Override
    public List<InventoryItem> getAllInventoryItems() {
        return inventoryItemRepository.findAll();
    }
}