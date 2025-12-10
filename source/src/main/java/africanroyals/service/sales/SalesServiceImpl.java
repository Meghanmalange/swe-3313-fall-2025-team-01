package africanroyals.service.sales;

import africanroyals.entity.Sale;
import africanroyals.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesServiceImpl implements SalesService {

    private final SaleRepository saleRepository;

    public SalesServiceImpl(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Override
    public List<Sale> findAll() {
        return saleRepository.findAll();
    }
}
