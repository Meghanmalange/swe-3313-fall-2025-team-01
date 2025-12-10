package africanroyals.config;

import africanroyals.entity.InventoryItem;
import africanroyals.entity.User;
import africanroyals.repository.InventoryItemRepository;
import africanroyals.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final InventoryItemRepository inventoryItemRepository;
    private final UserRepository userRepository;

    public DataInitializer(InventoryItemRepository inventoryItemRepository, UserRepository userRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Initialize users if database is empty
        if (userRepository.count() == 0) {
            User user1 = new User();
            user1.setUsername("customer");
            user1.setEmail("customer@africanroyals.com");
            user1.setFullName("Customer User");
            user1.setPasswordHash("password123");
            user1.setRole("USER");
            userRepository.save(user1);

            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@africanroyals.com");
            admin.setFullName("Admin User");
            admin.setPasswordHash("admin123");
            admin.setRole("ADMIN");
            userRepository.save(admin);

            System.out.println("✓ Initialized users in database");
        }

        // Initialize inventory items if database is empty
        if (inventoryItemRepository.count() == 0) {
            // Create sample African jewelry items
            InventoryItem item1 = new InventoryItem(
                    "Diamond Necklace",
                    "Exquisite handcrafted diamond necklace featuring traditional African patterns and modern elegance",
                    2000.0,
                    "https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?w=400&h=350&fit=crop",
                    false
            );

            InventoryItem item2 = new InventoryItem(
                    "Gold Bangle Bracelet",
                    "Beautiful 18k gold bangle with intricate African tribal designs",
                    1500.0,
                    "https://images.unsplash.com/photo-1611591437281-460bfbe1220a?w=400&h=350&fit=crop",
                    false
            );

            InventoryItem item3 = new InventoryItem(
                    "Beaded Collar Necklace",
                    "Traditional African beaded collar necklace in vibrant colors, handmade by local artisans",
                    350.0,
                    "https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?w=400&h=350&fit=crop",
                    false
            );

            InventoryItem item4 = new InventoryItem(
                    "Cowrie Shell Earrings",
                    "Elegant earrings featuring authentic cowrie shells, a symbol of wealth in African culture",
                    180.0,
                    "https://images.unsplash.com/photo-1535556116002-6281ff3e9f36?w=400&h=350&fit=crop",
                    false
            );

            InventoryItem item5 = new InventoryItem(
                    "Bronze Cuff Bracelet",
                    "Statement cuff bracelet in bronze with traditional engravings",
                    450.0,
                    "https://images.unsplash.com/photo-1573408301185-9146fe634ad0?w=400&h=350&fit=crop",
                    false
            );

            InventoryItem item6 = new InventoryItem(
                    "Ankara Print Ring",
                    "Modern ring featuring vibrant Ankara fabric patterns sealed in resin",
                    120.0,
                    "https://images.unsplash.com/photo-1605100804763-247f67b3557e?w=400&h=350&fit=crop",
                    false
            );

            InventoryItem item7 = new InventoryItem(
                    "Tribal Drop Earrings",
                    "Long drop earrings with geometric patterns inspired by African tribes",
                    280.0,
                    "https://images.unsplash.com/photo-1535556116002-6281ff3e9f36?w=400&h=350&fit=crop",
                    false
            );

            InventoryItem item8 = new InventoryItem(
                    "Vintage Amber Necklace",
                    "Rare vintage amber necklace from West Africa, a collector's piece",
                    3200.0,
                    "https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?w=400&h=350&fit=crop",
                    true
            );

            InventoryItem item9 = new InventoryItem(
                    "Wooden Bead Bracelet",
                    "Natural wooden beads hand-carved with traditional African symbols",
                    85.0,
                    "https://images.unsplash.com/photo-1611591437281-460bfbe1220a?w=400&h=350&fit=crop",
                    false
            );

            InventoryItem item10 = new InventoryItem(
                    "Silver Maasai Pendant",
                    "Authentic Maasai silver pendant with intricate beadwork",
                    650.0,
                    "https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?w=400&h=350&fit=crop",
                    false
            );

            // Save all items
            inventoryItemRepository.save(item1);
            inventoryItemRepository.save(item2);
            inventoryItemRepository.save(item3);
            inventoryItemRepository.save(item4);
            inventoryItemRepository.save(item5);
            inventoryItemRepository.save(item6);
            inventoryItemRepository.save(item7);
            inventoryItemRepository.save(item8);
            inventoryItemRepository.save(item9);
            inventoryItemRepository.save(item10);

            System.out.println("✓ Initialized " + inventoryItemRepository.count() + " jewelry items in database");
        } else {
            System.out.println("✓ Database already contains " + inventoryItemRepository.count() + " items");
        }
    }
}
