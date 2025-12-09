package africanroyals.repository;

import africanroyals.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    
    List<Sale> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    
    List<Sale> findByUserId(Long userId);
}