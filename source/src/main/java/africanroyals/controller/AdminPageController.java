package africanroyals.controller;

import africanroyals.entity.User;
import africanroyals.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminPageController {

    private final UserService userService;

    public AdminPageController(UserService userService) {
        this.userService = userService;
    }

    // GET search page
    @GetMapping("/promote-admin")
    public String showPromoteAdmin(@RequestParam(required = false) String query,
                                   Model model,
                                   HttpSession session) {

        if (!isAdmin(session)) {
            return "redirect:/catalog";
        }

        model.addAttribute("query", query);

        if (query != null && !query.isEmpty()) {
            User user = userService.findByEmailOrUsername(query);
            model.addAttribute("foundUser", user);
        }

        return "promote-admin"; // Thymeleaf template
    }

    // POST promote
    @PostMapping("/promote-admin")
    public String promoteAdmin(@RequestParam Long userId,
                               Model model,
                               HttpSession session) {

        if (!isAdmin(session)) {
            return "redirect:/catalog";
        }

        User user = userService.promoteToAdmin(userId);
        model.addAttribute("promoted", user);

        return "promote-admin";
    }

    // helper
    private boolean isAdmin(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return "ADMIN".equalsIgnoreCase(role);
    }
}
