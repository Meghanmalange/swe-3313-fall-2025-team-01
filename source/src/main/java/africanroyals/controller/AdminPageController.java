package africanroyals.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class AdminPageController {

    private boolean isAdmin(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return "ADMIN".equalsIgnoreCase(role);
    }

    @GetMapping("/add-item")
    public String showAddItem(HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        return "add-item";
    }

    @GetMapping("/promote-user")
    public String showPromoteUser(HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        return "promote-user";
    }
}

