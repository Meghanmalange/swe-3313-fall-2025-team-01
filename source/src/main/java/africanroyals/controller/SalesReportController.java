package africanroyals.controller;

import africanroyals.entity.Sale;
import africanroyals.service.sales.SalesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SalesReportController {

    private final SalesService salesService;

    public SalesReportController(SalesService salesService) {
        this.salesService = salesService;
    }

    private boolean isAdmin(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return "ADMIN".equalsIgnoreCase(role);
    }

    @GetMapping("/sales-report")
    public String showSalesReport(Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        List<SaleView> views = salesService.getAllSales().stream()
                .map(sale -> {
                    String name = sale.getUser() != null ? sale.getUser().getFullName() : "Unknown";
                    List<String> itemNames = sale.getItems() == null ? List.of() :
                            sale.getItems().stream().map(item -> item.getName()).collect(Collectors.toList());
                    double total = sale.getTotal() != null ? sale.getTotal() : 0.0;
                    return new SaleView(name, itemNames, total);
                })
                .collect(Collectors.toList());

        model.addAttribute("sales", views);
        return "sales-report";
    }

    public static class SaleView {
        private final String customerName;
        private final List<String> itemNames;
        private final double totalPrice;

        public SaleView(String customerName, List<String> itemNames, double totalPrice) {
            this.customerName = customerName;
            this.itemNames = itemNames;
            this.totalPrice = totalPrice;
        }

        public String getCustomerName() {
            return customerName;
        }

        public List<String> getItemNames() {
            return itemNames;
        }

        public double getTotalPrice() {
            return totalPrice;
        }
    }
}

