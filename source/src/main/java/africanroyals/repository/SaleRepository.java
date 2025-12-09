/* Include the following in this repository
Required to make the AdminController to work properly to generate SalesReports

package africanroyals.repository;


import africanroyals.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
*/

package africanroyals.repository;

import africanroyals.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
}

