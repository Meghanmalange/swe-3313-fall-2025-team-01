package africanroyals.service.inventory;

import africanroyals.entity.InventoryItem;
import africanroyals.repository.InventoryItemRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class InventoryServiceImpl implements InventoryService {
    private final InventoryItemRepository repo;

    public InventoryServiceImpl(InventoryItemRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<InventoryItem> getAvailableItems() {
        return repo.findAll();
    }

    @Override
    public List<InventoryItem> searchItems(String query) {
        if (query == null || query.isBlank()) {
            return getAvailableItems();
        }

        List<InventoryItem> byName = repo.searchByName(query);
        Set<InventoryItem> combined = new LinkedHashSet<>(byName);
        return List.copyOf(combined);
    }
}
