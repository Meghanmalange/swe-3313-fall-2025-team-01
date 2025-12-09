package africanroyals.controller;

import africanroyals.entity.InventoryItem;
import africanroyals.entity.Sale;
import africanroyals.entity.SaleItem;
import africanroyals.entity.User;
import africanroyals.repository.InventoryItemRepository;
import africanroyals.repository.SaleRepository;
import africanroyals.repository.UserRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/api")
public class AdminApiController {

    private final UserRepository userRepo;
    private final InventoryItemRepository itemRepo;
    private final SaleRepository saleRepo;

    public AdminApiController(UserRepository userRepo,
                              InventoryItemRepository itemRepo,
                              SaleRepository saleRepo) {
        this.userRepo = userRepo;
        this.itemRepo = itemRepo;
        this.saleRepo = saleRepo;
    }

    // USER MANAGEMENT

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @PostMapping("/promote/{id}")
    public String promoteUser(@PathVariable Long id) {
        User user = userRepo.findById(id).orElse(null);
        if (user == null) return "USER NOT FOUND";

        user.setAdmin(true);   // assuming your entity has setAdmin(boolean)
        userRepo.save(user);

        return "USER PROMOTED TO ADMIN";
    }

    // INVENTORY MANAGEMENT

    @PostMapping("/add-item")
    public String addItem(@RequestBody InventoryItem item) {
        itemRepo.save(item);
        return "ITEM ADDED";
    }

    @PutMapping("/update-item/{id}")
    public String updateItem(@PathVariable Long id, @RequestBody InventoryItem updated) {
        InventoryItem item = itemRepo.findById(id).orElse(null);
        if (item == null) return "ITEM NOT FOUND";

        item.setName(updated.getName());
        item.setDescription(updated.getDescription());
        item.setPrice(updated.getPrice());
        item.setQuantity(updated.getQuantity());

        itemRepo.save(item);
        return "ITEM UPDATED";
    }

    @DeleteMapping("/delete-item/{id}")
    public String deleteItem(@PathVariable Long id) {
        if (!itemRepo.existsById(id)) return "ITEM NOT FOUND";
        itemRepo.deleteById(id);
        return "ITEM DELETED";
    }

    @GetMapping("/search")
    public List<InventoryItem> search(@RequestParam String name) {
        return itemRepo.searchByName(name);
    }

    // SALES REPORTING

    @GetMapping("/sales")
    public List<Sale> viewAllSales() {
        return saleRepo.findAll();
    }

    @GetMapping("/sales/by-date")
    public List<Sale> getSalesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return saleRepo.findByTimestampBetween(start, end);
    }

    @GetMapping("/sales/total-revenue")
    public BigDecimal getTotalRevenue() {
        return saleRepo.findAll().stream()
                .map(Sale::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @GetMapping("/sales/item-performance")
    public Map<String, Integer> getItemSalesCount() {

        List<Sale> sales = saleRepo.findAll();
        Map<String, Integer> result = new HashMap<>();

        for (Sale sale : sales) {
            for (SaleItem item : sale.getItems()) {

                String name = item.getInventoryItem().getName();
                int qty = item.getQuantity();

                result.put(name, result.getOrDefault(name, 0) + qty);
            }
        }

        return result;
    }
}
