package africanroyals.service.impl;

import africanroyals.dto.LoginRequest;
import africanroyals.dto.RegisterRequest;
import africanroyals.entity.User;
import africanroyals.repository.UserRepository;
import africanroyals.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;

    public AuthServiceImpl(UserRepository userRepo,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authManager) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
    }

    @Override
    public String login(LoginRequest request) {

        String identifier = request.getIdentifier();
        String usernameToAuth = null;

        // Login with email
        if (identifier.contains("@")) {
            usernameToAuth = userRepo.findByEmail(identifier)
                    .map(User::getUsername)
                    .orElse(null);
        }
        // Login with username
        else {
            usernameToAuth = identifier;
        }

        if (usernameToAuth == null) {
            return "INVALID EMAIL OR USERNAME";
        }

        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            usernameToAuth,
                            request.getPassword()
                    )
            );
            return "LOGIN SUCCESS";
        } catch (Exception ex) {
            return "INVALID CREDENTIALS";
        }
    }

    @Override
    public String registerUser(RegisterRequest request) {

        if (userRepo.findByUsername(request.getUsername()).isPresent()) {
            return "USERNAME ALREADY EXISTS";
        }

        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            return "EMAIL ALREADY REGISTERED";
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAdmin(false);

        userRepo.save(user);
        return "USER REGISTERED SUCCESSFULLY";
    }
}


