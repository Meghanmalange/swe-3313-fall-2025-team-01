package africanroyals.security;

import africanroyals.entity.User;
import africanroyals.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    public CustomUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail)
            throws UsernameNotFoundException {

        // Login via username OR email
        User user = userRepo.findByUsername(usernameOrEmail)
                .orElseGet(() -> userRepo.findByEmail(usernameOrEmail)
                        .orElseThrow(() ->
                                new UsernameNotFoundException("User not found: " + usernameOrEmail)));

        return new CustomUserDetails(user);
    }
}