package africanroyals.service.impl;

import africanroyals.dto.LoginRequest;
import africanroyals.dto.RegisterRequest;
import africanroyals.model.Admin;
import africanroyals.repository.AdminRepository;
import africanroyals.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AdminRepository adminRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;

    public AuthServiceImpl(AdminRepository adminRepo,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authManager) {
        this.adminRepo = adminRepo;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
    }

    @Override
    public String login(LoginRequest request) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            return "LOGIN SUCCESS";
        } catch (Exception e) {
            return "INVALID CREDENTIALS";
        }
    }

    @Override
    public String registerAdmin(RegisterRequest request) {
        if (adminRepo.findByUsername(request.getUsername()).isPresent()) {
            return "USERNAME ALREADY EXISTS";
        }

        Admin newAdmin = new Admin(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword())
        );

        adminRepo.save(newAdmin);
        return "ADMIN REGISTERED";
    }
}

