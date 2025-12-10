package africanroyals.service.auth;

import africanroyals.dto.LoginRequest;
import africanroyals.dto.RegisterRequest;

public interface AuthService {
    String login(LoginRequest request);
    String registerUser(RegisterRequest request);
    String registerAdmin(RegisterRequest request);
}
