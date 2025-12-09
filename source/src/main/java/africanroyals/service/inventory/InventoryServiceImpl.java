package africanroyals.service.inventory;

import africanroyals.entity.InventoryItem;
import africanroyals.jewelry.repository.InventoryItemRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository repo;

    public InventoryServiceImpl(InventoryRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<InventoryItem> getAvailableItems() {
        return repo.findByIsSoldFalseOrderByPriceDesc();
    }

    @Override
    public List<InventoryItem> searchItems(String query) {
        if (query == null || query.isBlank()) {
            return getAvailableItems();
        }

        List<InventoryItem> byName =
                repo.findByIsSoldFalseAndNameContainingIgnoreCaseOrderByPriceDesc(query);

        List<InventoryItem> byDesc =
                repo.findByIsSoldFalseAndDescriptionContainingIgnoreCaseOrderByPriceDesc(query);

        // Combine results without duplicates
        Set<InventoryItem> combined = new LinkedHashSet<>();
        combined.addAll(byName);
        combined.addAll(byDesc);

        return new ArrayList<>(combined);
    }
}

