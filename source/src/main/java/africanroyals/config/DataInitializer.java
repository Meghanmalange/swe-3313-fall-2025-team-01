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

            User admin2 = new User();
            admin2.setUsername("megtheebaddest");
            admin2.setEmail("mmalange@students.kennesaw.edu");
            admin2.setFullName("Meghan Malange");
            admin2.setPasswordHash("$2a$10$UBSiRWYNOYwGNECo2FBXl.hjSW9/kDH3z4g4A3kSFoLPnAR9SjILy");
            admin2.setRole("ADMIN");
            userRepository.save(admin2);

            User admin3 = new User();
            admin3.setUsername("yxngjnr");
            admin3.setEmail("dtanyany@students.kennesaw.edu");
            admin3.setFullName("Douglas Tanyanyiwa");
            admin3.setPasswordHash("$2a$10$ckjhBajy/8Qhk50vUC7c8OHb98ndHFZa1pEshM/1R818Z2aG3SKBa");
            admin3.setRole("ADMIN");
            userRepository.save(admin3);

            User admin4 = new User();
            admin4.setUsername("Demean");
            admin4.setEmail("nmuzeren@students.kennesaw.edu");
            admin4.setFullName("Nyasha Muzerengi");
            admin4.setPasswordHash("$2a$10$Va4gfGiREaKgv3LdBKuF0uqKyYWJc2akfWZbTPVXBz3JyIBXJumO.");
            admin4.setRole("ADMIN");
            userRepository.save(admin4);

            User admin5= new User();
            admin5.setUsername("zclark16");
            admin5.setEmail("zclark16@students.kennesaw.edu");
            admin5.setFullName("Zion Clark");
            admin5.setPasswordHash("$2a$10$hG9uooVrYMFLjxL6M4VbYe2LiYttVeFK0WcEyw15ba/nJWowex8HW.");
            admin5.setRole("ADMIN");
            userRepository.save(admin5);

            User admin6= new User();
            admin6.setUsername("aaliy4hslvr");
            admin6.setEmail("auchend1@students.kennesaw.edu");
            admin6.setFullName("Aaliyah Uchendu");
            admin6.setPasswordHash("$2a$10$8fDW171oWenz6p65FUq4wOIYkdrkyx3ThF9jkeKRPePYQfDNeZnJ6");
            admin6.setRole("ADMIN");
            userRepository.save(admin6);

            System.out.println("✓ Initialized users in database");
        }

        // Initialize inventory items if database is empty
        if (inventoryItemRepository.count() == 0) {
            // Create sample African jewelry items using local images
            InventoryItem item1 = new InventoryItem(
                    "Diamond Necklace",
                    "Exquisite handcrafted diamond necklace featuring traditional African patterns and modern elegance",
                    2000.0,
                    "/images/diamond-necklace.png",
                    false
            );

            InventoryItem item2 = new InventoryItem(
                    "Ancient Gold Bracelet",
                    "Vintage-inspired gold bracelet with intricate ancient African designs and patterns",
                    1800.0,
                    "/images/ancient-gold-bracelet.png",
                    false
            );

            InventoryItem item3 = new InventoryItem(
                    "Gold Bead Necklace",
                    "Traditional African gold bead necklace, handcrafted with authentic techniques",
                    1200.0,
                    "/images/gold-bead-necklace.png",
                    false
            );

            InventoryItem item4 = new InventoryItem(
                    "Gold Bracelet",
                    "Elegant 18k gold bracelet featuring classic African tribal motifs",
                    1500.0,
                    "/images/gold-bracelet.png",
                    false
            );

            InventoryItem item5 = new InventoryItem(
                    "Gold Necklace",
                    "Stunning gold necklace with contemporary African design elements",
                    2200.0,
                    "/images/gold-necklace.png",
                    false
            );

            InventoryItem item6 = new InventoryItem(
                    "Shell Earrings",
                    "Beautiful earrings featuring authentic African shells, a symbol of natural beauty",
                    280.0,
                    "/images/shell-earrings.png",
                    false
            );

            InventoryItem item7 = new InventoryItem(
                    "Stylish Black Necklace",
                    "Modern black necklace with bold African-inspired geometric patterns",
                    850.0,
                    "/images/stylish-black-necklace.png",
                    false
            );

            InventoryItem item8 = new InventoryItem(
                    "Tribal Ring",
                    "Handcrafted tribal ring with traditional African symbols and engravings",
                    450.0,
                    "/images/tribal-ring.png",
                    false
            );

            InventoryItem item9 = new InventoryItem(
                    "Zimbabwe Necklace",
                    "Authentic Zimbabwean necklace showcasing traditional craftsmanship and cultural heritage",
                    950.0,
                    "/images/zimbabwe-necklace.png",
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

            System.out.println("✓ Initialized " + inventoryItemRepository.count() + " jewelry items in database");
        } else {
            System.out.println("✓ Database already contains " + inventoryItemRepository.count() + " items");
        }
    }
}
