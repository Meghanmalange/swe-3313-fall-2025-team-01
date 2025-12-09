package africanroyals.service;
import africanroyals.entity.User;
import africanroyals.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ---------- SIGN UP (create user with USER role) ----------
    public User createUser(String email,
                           String username,
                           String fullName,
                           String password) {

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setFullName(fullName);
        user.setPassword(password); // later: hash this
        user.setRole("USER");

        return userRepository.save(user);
    }

    // ---------- LOGIN (username OR email + password) ----------
    public User validateLogin(String login, String password) {
        // login could be username or email
        Optional<User> byUsername = userRepository.findByUsernameIgnoreCase(login);
        Optional<User> byEmail = userRepository.findByEmailIgnoreCase(login);

        User user = byUsername.orElseGet(byEmail::orElse);

        if (user == null) {
            return null;
        }

        // Plain-text comparison for now (you can switch to BCrypt later)
        if (!user.getPassword().equals(password)) {
            return null;
        }

        return user;
    }

    // ---------- FIND BY EMAIL OR USERNAME (for Promote Admin page) ----------
    public User findByEmailOrUsername(String query) {
        if (query == null || query.isBlank()) {
            return null;
        }

        return userRepository.findByEmailIgnoreCase(query)
                .or(() -> userRepository.findByUsernameIgnoreCase(query))
                .orElse(null);
    }

    // ---------- PROMOTE USER TO ADMIN ----------
    public User promoteToAdmin(Long userId) {
        Optional<User> opt = userRepository.findById(userId);
        if (opt.isEmpty()) {
            return null;
        }

        User user = opt.get();
        user.setRole("ADMIN");
        return userRepository.save(user);
    }

    // ---------- SIMPLE GET BY ID ----------
    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
