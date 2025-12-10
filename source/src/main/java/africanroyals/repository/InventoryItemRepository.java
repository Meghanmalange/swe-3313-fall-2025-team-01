package africanroyals.repository;

import africanroyals.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    List<InventoryItem> findByIsSold(Boolean isSold);
    List<InventoryItem> findByNameContainingIgnoreCase(String name);
    Optional<InventoryItem> findByNameIgnoreCase(String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<InventoryItem> findWithLockingById(Long id);
}
