package africanroyals.repository;

import africanroyals.entity.Sale;
import africanroyals.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findByUser(User user);
    List<Sale> findByUserId(Long userId);
}
