package africanroyals.controller;

import africanroyals.inventory.InventoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/inventory")
public class InventoryController {
    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    // Show all available items
    @GetMapping
    public String showInventory(Model model) {
        model.addAttribute("items", service.getAvailableItems());
        return "inventory";
    }

    // Search items
    @GetMapping("/search")
    public String searchInventory(@RequestParam("query") String query, Model model) {
        model.addAttribute("items", service.searchItems(query));
        model.addAttribute("query", query);
        return "searchResults";
    }
}
