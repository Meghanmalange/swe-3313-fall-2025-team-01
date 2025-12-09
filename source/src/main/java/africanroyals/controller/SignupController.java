package africanroyals.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@PostMapping("/signup")
public String SignupController(
        @RequestParam String email,
        @RequestParam String username,
        @RequestParam String fullName,
        @RequestParam String password
){
    userService.createUser(email, username, fullName, password);

    return "redirect:/catalog";
}

