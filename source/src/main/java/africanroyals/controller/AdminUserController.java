package africanroyals.controller;

import africanroyals.entity.User;
import africanroyals.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class AdminUserController {

    private final UserRepository userRepository;

    public AdminUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private boolean isAdmin(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return "ADMIN".equalsIgnoreCase(role);
    }

    @PostMapping("/admin/promote-user/search")
    public String findUser(@RequestParam("identifier") String identifier,
                           HttpSession session,
                           Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute("identifier", identifier);

        Optional<User> userOpt = userRepository.findByUsername(identifier)
                .or(() -> userRepository.findByEmail(identifier));

        if (userOpt.isEmpty()) {
            model.addAttribute("searchError", "No user found with that username or email.");
            return "promote-user";
        }

        model.addAttribute("promoteCandidate", userOpt.get());
        return "promote-user";
    }

    @PostMapping("/admin/promote-user/promote")
    public String promoteUser(@RequestParam("userId") Long userId,
                              HttpSession session,
                              Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            model.addAttribute("searchError", "User not found.");
            return "promote-user";
        }

        user.setRole("ADMIN");
        userRepository.save(user);

        model.addAttribute("promotionSuccess", user.getFullName() + " has been promoted to admin.");
        model.addAttribute("promoteCandidate", user);
        return "promote-user";
    }
}

