package africanroyals.controller;
import africanroyals.repository.SaleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class SalesReportController {

    private final SaleRepository saleRepository;

    public SalesReportController(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @GetMapping("/sales-report")
    public String salesReport(Model model) {
        model.addAttribute("sales", saleRepository.findAll());
        return "sales-report"; // sales-report.html
    }
}
