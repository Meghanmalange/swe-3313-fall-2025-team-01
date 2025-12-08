package africanroyals.security;

import africanroyals.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        // ROLE_USER is always granted
        SimpleGrantedAuthority baseRole = new SimpleGrantedAuthority("ROLE_USER");

        if (user.isAdmin()) {
            // Admin gets both roles
            SimpleGrantedAuthority adminRole = new SimpleGrantedAuthority("ROLE_ADMIN");
            return List.of(baseRole, adminRole);
        }

        return List.of(baseRole);
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // Login is done with username always
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public String getEmail() {
        return user.getEmail();
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
