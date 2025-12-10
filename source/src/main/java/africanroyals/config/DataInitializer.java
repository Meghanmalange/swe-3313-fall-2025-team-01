package africanroyals.config;

import africanroyals.entity.User;
import africanroyals.entity.InventoryItem;
import africanroyals.repository.UserRepository;
import africanroyals.repository.InventoryItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initializeData(
            UserRepository userRepo,
            InventoryItemRepository itemRepo,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            // ---- Seed Admin Users ----
            seedAdmin(userRepo, passwordEncoder,
                    "Admin One", "admin1@example.com", "admin1", "password1");

            seedAdmin(userRepo, passwordEncoder,
                    "Admin Two", "admin2@example.com", "admin2", "password2");

            seedAdmin(userRepo, passwordEncoder,
                    "Admin Three", "admin3@example.com", "admin3", "password3");

            seedAdmin(userRepo, passwordEncoder,
                    "Admin Four", "admin4@example.com", "admin4", "password4");

            seedAdmin(userRepo, passwordEncoder,
                    "Admin Five", "admin5@example.com", "admin5", "password5");

            // ---- Seed Inventory Items ----
            seedItem(itemRepo,
                    "Royal Necklace",
                    "24K gold-plated handmade necklace",
                    new BigDecimal("1200.00"),
                    5);

            seedItem(itemRepo,
                    "Zulu Bracelet",
                    "Traditional beaded bracelet created by artisans",
                    new BigDecimal("80.00"),
                    20);
        };
    }

    private void seedAdmin(
            UserRepository repo,
            PasswordEncoder encoder,
            String fullName,
            String email,
            String username,
            String rawPassword) {

        boolean exists = repo.findByUsername(username).isPresent()
                || repo.findByEmail(email).isPresent();

        if (!exists) {
            User admin = new User();
            admin.setFullName(fullName);
            admin.setEmail(email);
            admin.setUsername(username);
            admin.setPassword(encoder.encode(rawPassword));
            admin.setAdmin(true);

            repo.save(admin);
        }
    }

    private void seedItem(
            InventoryItemRepository repo,
            String name,
            String description,
            BigDecimal price,
            int quantity
    ) {

        boolean exists = repo.findByNameIgnoreCase(name).isPresent();
        if (!exists) {
            InventoryItem item = new InventoryItem();
            item.setName(name);
            item.setDescription(description);
            item.setPrice(price);
            item.setQuantity(quantity);

            repo.save(item);
        }
    }
}
