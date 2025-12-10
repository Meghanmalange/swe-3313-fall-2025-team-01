package africanroyals.controller;

import africanroyals.dto.LoginRequest;
import africanroyals.dto.RegisterRequest;
import africanroyals.service.auth.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register-admin")
    public String registerAdmin(@RequestBody RegisterRequest request) {
        return authService.registerAdmin(request);
    }
}


