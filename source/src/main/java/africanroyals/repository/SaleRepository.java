package africanroyals.repository;

import africanroyals.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
