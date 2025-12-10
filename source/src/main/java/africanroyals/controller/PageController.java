package africanroyals.controller;

import africanroyals.Application;
import africanroyals.dto.CheckoutRequest;
import africanroyals.entity.InventoryItem;
import africanroyals.entity.Sale;
import africanroyals.entity.User;
import africanroyals.model.CartCheckout.Cart;
import africanroyals.model.CartCheckout.CartItem;
import africanroyals.model.CartCheckout.PriceDetails;
import africanroyals.model.CartCheckout.Receipt;
import africanroyals.model.CartCheckout.ShippingOption;
import africanroyals.repository.InventoryItemRepository;
import africanroyals.repository.UserRepository;
import africanroyals.service.cart.CartService;
import africanroyals.service.checkout.CheckoutService;
import africanroyals.service.sales.SalesService;
import africanroyals.util.PriceCalculator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import java.nio.file.Paths;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class PageController {

    private final UserRepository userRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final CartService cartService;
    private final PriceCalculator priceCalculator;
    private final CheckoutService checkoutService;
    private final SalesService salesService;
    private final ResourceLoader resourceLoader;
    private final BCryptPasswordEncoder passwordEncoder;

    public PageController(UserRepository userRepository, InventoryItemRepository inventoryItemRepository, CartService cartService, PriceCalculator priceCalculator, CheckoutService checkoutService, SalesService salesService, ResourceLoader resourceLoader) {
        this.userRepository = userRepository;
        this.inventoryItemRepository = inventoryItemRepository;
        this.cartService = cartService;
        this.priceCalculator = priceCalculator;
        this.checkoutService = checkoutService;
        this.salesService = salesService;
        this.resourceLoader = resourceLoader;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @GetMapping({"/", "/index"})
    public String showIndex() {
        return "index";
    }

    @GetMapping("/login")
    public String showLogin(Model model, HttpSession session) {
        // If already logged in, redirect based on role
        // This prevents multiple sessions and ensures only one account is active
        String role = (String) session.getAttribute("role");
        if (role != null) {
            if ("ADMIN".equals(role)) {
                return "redirect:/admin-catalog";
            } else {
                return "redirect:/catalog";
            }
        }
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model,
            HttpSession session) {

        // Validate input
        if (username == null || username.trim().isEmpty()) {
            model.addAttribute("error", "Username is required");
            return "login";
        }

        if (password == null || password.trim().isEmpty()) {
            model.addAttribute("error", "Password is required");
            return "login";
        }

        // Find user by username
        Optional<User> userOpt = userRepository.findByUsername(username.trim());

        // Validate user exists and password matches
        if (userOpt.isEmpty()) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }

        User user = userOpt.get();

        // Verify password - supports both BCrypt hashed passwords and plain text (for backward compatibility)
        String storedHash = user.getPasswordHash();
        boolean passwordMatches = false;

        // Check if password is BCrypt hashed (starts with $2a$, $2b$, or $2y$)
        if (storedHash != null && (storedHash.startsWith("$2a$") || storedHash.startsWith("$2b$") || storedHash.startsWith("$2y$"))) {
            // Use BCrypt to verify hashed password
            passwordMatches = passwordEncoder.matches(password, storedHash);
        } else {
            // Plain text comparison (for backward compatibility with existing accounts)
            passwordMatches = storedHash != null && storedHash.equals(password);
        }

        if (!passwordMatches) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }

        // If already logged in as different user, update session
        // Otherwise, just set the attributes
        session.setAttribute("userId", user.getId());
        session.setAttribute("username", user.getUsername());
        session.setAttribute("role", user.getRole());
        session.setAttribute("email", user.getEmail());
        session.setAttribute("fullName", user.getFullName());

        // Redirect based on role
        if ("ADMIN".equals(user.getRole())) {
            return "redirect:/admin-catalog";
        } else {
            return "redirect:/catalog";
        }
    }

    @GetMapping("/signup")
    public String showSignup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String handleSignup(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("fullName") String fullName,
            @RequestParam("password") String password,
            Model model,
            HttpSession session) {

        // Validate input
        if (username == null || username.trim().isEmpty()) {
            model.addAttribute("error", "Username is required");
            return "signup";
        }

        if (email == null || email.trim().isEmpty()) {
            model.addAttribute("error", "Email is required");
            return "signup";
        }

        if (fullName == null || fullName.trim().isEmpty()) {
            model.addAttribute("error", "Full name is required");
            return "signup";
        }

        if (password == null || password.trim().isEmpty()) {
            model.addAttribute("error", "Password is required");
            return "signup";
        }

        if (password.length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters long");
            return "signup";
        }

        // Check if username already exists
        if (userRepository.existsByUsername(username.trim())) {
            model.addAttribute("error", "Username already exists. Please choose a different username.");
            return "signup";
        }

        // Check if email already exists
        if (userRepository.existsByEmail(email.trim())) {
            model.addAttribute("error", "Email already exists. Please use a different email.");
            return "signup";
        }

        // Create new user
        User newUser = new User();
        newUser.setUsername(username.trim());
        newUser.setEmail(email.trim());
        newUser.setFullName(fullName.trim());

        // Hash password with BCrypt
        String hashedPassword = passwordEncoder.encode(password);
        newUser.setPasswordHash(hashedPassword);

        // All new signups are regular users (not admin)
        newUser.setRole("USER");

        // Save user to database
        User savedUser = userRepository.save(newUser);

        // Store user in session
        session.setAttribute("userId", savedUser.getId());
        session.setAttribute("username", savedUser.getUsername());
        session.setAttribute("role", savedUser.getRole());
        session.setAttribute("email", savedUser.getEmail());
        session.setAttribute("fullName", savedUser.getFullName());

        // Redirect to catalog (since they are regular users, not admin)
        return "redirect:/catalog";
    }

    @GetMapping("/catalog")
    public String showCatalog(HttpSession session, Model model, @RequestParam(required = false) String added) {
        // Check if user is logged in (interceptor will handle redirect, but double-check here)
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        
        Long userId = (Long) session.getAttribute("userId");
        
        // Fetch all inventory items from database
        List<InventoryItem> items = inventoryItemRepository.findAll();
        model.addAttribute("products", items);
        
        // Get user's cart to check which items are already in cart
        Cart cart = cartService.getCartForUser(userId);
        Set<Long> cartItemIds = cart.getItems().stream()
                .map(item -> item.getItemId())
                .collect(Collectors.toSet());
        model.addAttribute("cartItemIds", cartItemIds);
        
        // Show toast notification if item was just added
        if (added != null && !added.isEmpty()) {
            model.addAttribute("addedProductName", added);
        }
        
        return "catalog";
    }

    @GetMapping("/admin-catalog")
    public String showAdminCatalog(HttpSession session, Model model, @RequestParam(required = false) String added) {
        // Check if user is logged in and is admin
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        String role = (String) session.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return "redirect:/catalog";
        }
        
        Long userId = (Long) session.getAttribute("userId");
        
        // Fetch all inventory items from database (admin sees all items, including sold ones)
        List<InventoryItem> items = inventoryItemRepository.findAll();
        model.addAttribute("products", items);
        
        // Get user's cart to check which items are already in cart
        Cart cart = cartService.getCartForUser(userId);
        Set<Long> cartItemIds = cart.getItems().stream()
                .map(item -> item.getItemId())
                .collect(Collectors.toSet());
        model.addAttribute("cartItemIds", cartItemIds);
        
        // Show toast notification if item was just added
        if (added != null && !added.isEmpty()) {
            model.addAttribute("addedProductName", added);
        }
        
        return "admin-catalog";
    }

    @PostMapping("/cart/add")
    public String addToCart(
            @RequestParam("productId") Long productId,
            @RequestParam(value = "fromSearch", required = false) String fromSearch,
            @RequestParam(value = "searchQuery", required = false) String searchQuery,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        // Check if user is logged in
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        // Find the inventory item
        Optional<InventoryItem> itemOpt = inventoryItemRepository.findById(productId);
        if (itemOpt.isEmpty()) {
            // Item not found, redirect back appropriately
            if ("true".equals(fromSearch)) {
                redirectAttributes.addAttribute("query", searchQuery != null ? searchQuery : "");
                return "redirect:/search";
            }
            String redirectUrl = "ADMIN".equals(session.getAttribute("role")) 
                    ? "redirect:/admin-catalog" 
                    : "redirect:/catalog";
            return redirectUrl;
        }
        
        InventoryItem item = itemOpt.get();
        
        // Check if item is sold - can't add sold items to cart
        if (item.getIsSold() != null && item.getIsSold()) {
            if ("true".equals(fromSearch)) {
                redirectAttributes.addAttribute("query", searchQuery != null ? searchQuery : "");
                return "redirect:/search";
            }
            String redirectUrl = "ADMIN".equals(session.getAttribute("role")) 
                    ? "redirect:/admin-catalog" 
                    : "redirect:/catalog";
            return redirectUrl;
        }
        
        // Get user's cart to check if item is already in cart
        Cart cart = cartService.getCartForUser(userId);
        boolean alreadyInCart = cart.getItems().stream()
                .anyMatch(ci -> ci.getItemId().equals(productId));
        
        // Add item to cart if not already in cart
        if (!alreadyInCart) {
            cartService.addItemToCart(
                    userId,
                    item.getId(),
                    item.getName(),
                    BigDecimal.valueOf(item.getPrice())
            );
            
            // Add product name to redirect attributes for toast notification
            redirectAttributes.addAttribute("added", item.getName());
        }
        
        // Redirect back appropriately - if from search, go back to search with query
        if ("true".equals(fromSearch)) {
            if (searchQuery != null && !searchQuery.isEmpty()) {
                redirectAttributes.addAttribute("query", searchQuery);
            }
            return "redirect:/search";
        }
        
        // Otherwise redirect to catalog
        String redirectUrl = "ADMIN".equals(session.getAttribute("role")) 
                ? "redirect:/admin-catalog" 
                : "redirect:/catalog";
        return redirectUrl;
    }
    
    @GetMapping("/cart/remove")
    public String removeFromCart(
            @RequestParam("productId") Long productId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        // Check if user is logged in
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        // Remove item from cart
        cartService.removeItemFromCart(userId, productId);
        
        // Redirect back to checkout
        return "redirect:/checkout";
    }

    @GetMapping("/search")
    public String showSearch(
            @RequestParam(required = false) String query,
            HttpSession session,
            Model model,
            @RequestParam(required = false) String added) {
        
        // Check if user is logged in
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        String role = (String) session.getAttribute("role");
        boolean isAdmin = "ADMIN".equals(role);
        model.addAttribute("isAdmin", isAdmin);
        
        // Get all inventory items
        List<InventoryItem> allItems = inventoryItemRepository.findAll();
        
        // Filter items based on search query (case-insensitive, substring matching in order)
        List<InventoryItem> filteredItems = allItems;
        if (query != null && !query.trim().isEmpty()) {
            String searchTerm = query.trim().toLowerCase();
            filteredItems = allItems.stream()
                    .filter(item -> item.getName() != null && 
                            item.getName().toLowerCase().contains(searchTerm))
                    .collect(Collectors.toList());
        }
        
        model.addAttribute("products", filteredItems);
        model.addAttribute("searchQuery", query != null ? query : "");
        
        // Get user's cart to check which items are already in cart
        Cart cart = cartService.getCartForUser(userId);
        Set<Long> cartItemIds = cart.getItems().stream()
                .map(item -> item.getItemId())
                .collect(Collectors.toSet());
        model.addAttribute("cartItemIds", cartItemIds);
        
        // Show toast notification if item was just added
        if (added != null && !added.isEmpty()) {
            model.addAttribute("addedProductName", added);
        }
        
        return "search";
    }

    @GetMapping("/checkout")
    public String showCheckout(HttpSession session, Model model) {
        // Check if user is logged in
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        // Get user's cart
        Cart cart = cartService.getCartForUser(userId);
        
        // Convert cart items to display format with image URLs
        List<CartDisplayItem> cartProducts = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            Optional<InventoryItem> itemOpt = inventoryItemRepository.findById(cartItem.getItemId());
            if (itemOpt.isPresent()) {
                InventoryItem item = itemOpt.get();
                CartDisplayItem displayItem = new CartDisplayItem();
                displayItem.setItemId(cartItem.getItemId());
                displayItem.setName(cartItem.getName());
                displayItem.setPrice(cartItem.getUnitPrice().doubleValue());
                displayItem.setImagePath(item.getImageUrl() != null ? item.getImageUrl() : "/images/diamond-necklace.png");
                cartProducts.add(displayItem);
            }
        }
        
        model.addAttribute("cartProducts", cartProducts);
        model.addAttribute("isAdmin", "ADMIN".equals(session.getAttribute("role")));
        
        // Calculate prices with default GROUND shipping
        if (!cart.getItems().isEmpty()) {
            PriceDetails priceDetails = priceCalculator.calculate(cart, ShippingOption.GROUND);
            model.addAttribute("subtotal", priceDetails.getSubtotal().doubleValue());
            model.addAttribute("tax", priceDetails.getTax().doubleValue());
            model.addAttribute("totalBeforeShipping", priceDetails.getSubtotal().add(priceDetails.getTax()).doubleValue());
        } else {
            model.addAttribute("subtotal", 0.0);
            model.addAttribute("tax", 0.0);
            model.addAttribute("totalBeforeShipping", 0.0);
        }
        
        return "checkout";
    }
    
    // Helper class for cart display items
    private static class CartDisplayItem {
        private Long itemId;
        private String name;
        private Double price;
        private String imagePath;
        
        public Long getItemId() { return itemId; }
        public void setItemId(Long itemId) { this.itemId = itemId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }
        public String getImagePath() { return imagePath; }
        public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    }

    @PostMapping("/checkout/complete")
    public String handleCheckoutComplete(
            @RequestParam("fullName") String fullName,
            @RequestParam("shipAddress") String shipAddress,
            @RequestParam(value = "shipAdditional", required = false) String shipAdditional,
            @RequestParam("shipCity") String shipCity,
            @RequestParam("shipState") String shipState,
            @RequestParam("shipZip") String shipZip,
            @RequestParam("shipCountry") String shipCountry,
            @RequestParam("shippingMethod") String shippingMethod,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // Check if user is logged in
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        // Get user's cart
        Cart cart = cartService.getCartForUser(userId);
        
        // Check if cart is empty
        if (cart.getItems().isEmpty()) {
            return "redirect:/checkout";
        }

        // Parse shipping option
        ShippingOption shippingOption;
        try {
            shippingOption = ShippingOption.valueOf(shippingMethod);
        } catch (IllegalArgumentException e) {
            shippingOption = ShippingOption.GROUND; // Default to GROUND
        }

        // Calculate prices
        PriceDetails priceDetails = priceCalculator.calculate(cart, shippingOption);

        // Create checkout request
        CheckoutRequest checkoutRequest = new CheckoutRequest();
        checkoutRequest.setUserId(userId);
        checkoutRequest.setShippingOption(shippingOption);
        checkoutRequest.setCardHolderName(fullName);

        // Perform checkout (creates receipt and sale, marks items as sold, clears cart)
        Receipt receipt = checkoutService.checkout(checkoutRequest);

        // Update sale with shipping address details
        if (receipt.getOrderId() != null) {
            salesService.updateSaleShippingDetails(
                    receipt.getOrderId(),
                    shipAddress,
                    shipCity,
                    shipState,
                    shipZip,
                    shipAdditional
            );
        }

        // Pass data to complete-purchase page
        redirectAttributes.addFlashAttribute("buyerName", fullName);
        redirectAttributes.addFlashAttribute("finalTotal", priceDetails.getGrandTotal().doubleValue());
        redirectAttributes.addFlashAttribute("orderId", receipt.getOrderId());
        
        // Get item names for display
        List<String> itemNames = cart.getItems().stream()
                .map(CartItem::getName)
                .collect(Collectors.toList());
        redirectAttributes.addFlashAttribute("itemNames", itemNames);

        // Redirect to the thank-you page
        return "redirect:/complete-purchase";
    }

    @GetMapping("/complete-purchase")
    public String completePurchase(HttpSession session, Model model) {
        // Check if user is logged in
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        
        // Pass admin status for proper redirect
        model.addAttribute("isAdmin", "ADMIN".equals(session.getAttribute("role")));
        
        return "complete-purchase";
    }

    @GetMapping("/add-item")
    public String showAddItem(HttpSession session, Model model) {
        // Check if user is admin
        String role = (String) session.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return "redirect:/catalog";
        }
        
        // Show success message if item was just added
        if (model.containsAttribute("itemAdded")) {
            model.addAttribute("itemAdded", true);
        }
        
        return "add-item";
    }
    
    @PostMapping("/admin/add-item")
    public String handleAddItem(
            @RequestParam("name") String name,
            @RequestParam("price") String priceStr,
            @RequestParam("imageFile") MultipartFile imageFile,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        // Check if user is admin
        String role = (String) session.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return "redirect:/catalog";
        }
        
        // Validate all fields are filled
        if (name == null || name.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Name is required");
            return "redirect:/add-item";
        }
        
        if (priceStr == null || priceStr.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Price is required");
            return "redirect:/add-item";
        }
        
        if (imageFile == null || imageFile.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Image is required");
            return "redirect:/add-item";
        }
        
        // Validate price is numeric
        Double price;
        try {
            price = Double.parseDouble(priceStr.trim());
            if (price <= 0) {
                redirectAttributes.addFlashAttribute("error", "Price must be greater than 0");
                return "redirect:/add-item";
            }
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("error", "Price must be a valid number");
            return "redirect:/add-item";
        }
        
        // Handle image upload
        String imageUrl = null;
        try {
            if (!imageFile.isEmpty()) {
                // Get original filename
                String originalFilename = imageFile.getOriginalFilename();
                if (originalFilename == null || originalFilename.isEmpty()) {
                    redirectAttributes.addFlashAttribute("error", "Invalid image file");
                    return "redirect:/add-item";
                }
                
                // Generate unique filename to avoid conflicts
                String fileExtension = "";
                int lastDotIndex = originalFilename.lastIndexOf('.');
                if (lastDotIndex > 0) {
                    fileExtension = originalFilename.substring(lastDotIndex);
                }
                String uniqueFilename = System.currentTimeMillis() + "_" + 
                        name.trim().replaceAll("[^a-zA-Z0-9]", "_") + fileExtension;
                
                // Get the absolute path to the resources/static/images directory
                java.io.File uploadDir = null;
                
                try {
                    // Method 1: Try to get from classpath resource (works in development)
                    Resource imagesResource = resourceLoader.getResource("classpath:static/images");
                    if (imagesResource.exists()) {
                        try {
                            uploadDir = imagesResource.getFile();
                        } catch (Exception e) {
                            // File might be in JAR, can't use getFile()
                        }
                    }
                    
                    // Method 2: Get static directory and create images subdirectory
                    if (uploadDir == null) {
                        Resource staticResource = resourceLoader.getResource("classpath:static");
                        if (staticResource.exists()) {
                            try {
                                java.io.File staticDir = staticResource.getFile();
                                uploadDir = new java.io.File(staticDir, "images");
                            } catch (Exception e) {
                                // File might be in JAR
                            }
                        }
                    }
                } catch (Exception ex) {
                    // Classpath resource might be in JAR, use file system approach
                }
                
                // Method 3: Find project root using Application class location
                if (uploadDir == null || !uploadDir.exists()) {
                    try {
                        // Get the location of the Application class
                        java.net.URL classUrl = Application.class.getProtectionDomain()
                                .getCodeSource().getLocation();
                        if (classUrl != null && "file".equals(classUrl.getProtocol())) {
                            java.io.File codeSource = new java.io.File(classUrl.toURI());
                            java.io.File projectRoot = codeSource;
                            
                            // Navigate from target/classes or bin back to source directory
                            // Look for "source" directory or "src" directory
                            while (projectRoot != null) {
                                // Check if we're in the source directory
                                if (projectRoot.getName().equals("source") && 
                                    new java.io.File(projectRoot, "src").exists()) {
                                    break;
                                }
                                // Check if src exists in current directory
                                if (new java.io.File(projectRoot, "src").exists()) {
                                    break;
                                }
                                projectRoot = projectRoot.getParentFile();
                            }
                            
                            if (projectRoot != null && projectRoot.exists()) {
                                uploadDir = new java.io.File(projectRoot, "src/main/resources/static/images");
                            }
                        }
                    } catch (Exception e) {
                        // Fall through to next method
                    }
                }
                
                // Method 4: Use known workspace path structure
                if (uploadDir == null || !uploadDir.exists()) {
                    String homeDir = System.getProperty("user.home");
                    java.io.File workspacePath = Paths.get(homeDir, "swe-3313-fall-2025-team-01", "source").toFile();
                    if (workspacePath.exists()) {
                        uploadDir = new java.io.File(workspacePath, "src/main/resources/static/images");
                    }
                }
                
                // Method 5: Last resort - navigate from user.dir to find source
                if (uploadDir == null || !uploadDir.exists()) {
                    String userDir = System.getProperty("user.dir");
                    java.io.File current = new java.io.File(userDir);
                    java.io.File sourceDir = null;
                    
                    // Search up to 5 levels to find "source" directory
                    for (int i = 0; i < 5 && current != null; i++) {
                        if (current.getName().equals("source") && 
                            new java.io.File(current, "src").exists()) {
                            sourceDir = current;
                            break;
                        }
                        current = current.getParentFile();
                    }
                    
                    if (sourceDir != null) {
                        uploadDir = new java.io.File(sourceDir, "src/main/resources/static/images");
                    } else {
                        // Final fallback
                        uploadDir = new java.io.File(userDir, "src/main/resources/static/images");
                    }
                }
                
                // Ensure directory exists
                if (!uploadDir.exists()) {
                    boolean created = uploadDir.mkdirs();
                    if (!created && !uploadDir.exists()) {
                        redirectAttributes.addFlashAttribute("error", 
                            "Could not create images directory. Tried: " + uploadDir.getAbsolutePath());
                        return "redirect:/add-item";
                    }
                }
                
                java.io.File destFile = new java.io.File(uploadDir, uniqueFilename);
                imageFile.transferTo(destFile);
                
                // Store the URL path (relative to /images/)
                imageUrl = "/images/" + uniqueFilename;
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload image: " + e.getMessage());
            return "redirect:/add-item";
        }
        
        // Create and save inventory item
        InventoryItem newItem = new InventoryItem();
        newItem.setName(name.trim());
        newItem.setPrice(price);
        newItem.setImageUrl(imageUrl);
        newItem.setIsSold(false);
        newItem.setDescription(""); // Optional field
        
        try {
            inventoryItemRepository.save(newItem);
            
            // Add success message
            redirectAttributes.addFlashAttribute("itemAdded", true);
            redirectAttributes.addFlashAttribute("itemName", name.trim());
            
            return "redirect:/add-item";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to save item: " + e.getMessage());
            return "redirect:/add-item";
        }
    }

    @GetMapping("/sales-report")
    public String showSalesReport(HttpSession session, Model model) {
        // Check if user is admin
        String role = (String) session.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return "redirect:/catalog";
        }
        
        // Get all sales from database
        List<Sale> allSales = salesService.getAllSales();
        
        // Sort by ID descending (most recent first - highest ID is most recent)
        // Handle null IDs by putting them at the end
        if (allSales != null && !allSales.isEmpty()) {
            allSales.sort((s1, s2) -> {
                Long id1 = s1 != null && s1.getId() != null ? s1.getId() : 0L;
                Long id2 = s2 != null && s2.getId() != null ? s2.getId() : 0L;
                return Long.compare(id2, id1); // Descending order
            });
        }
        
        // Pass sales to view (ensure it's never null)
        model.addAttribute("sales", allSales != null ? allSales : new ArrayList<>());
        
        return "sales-report";
    }

    @GetMapping("/promote-user")
    public String showPromoteUser(HttpSession session, Model model) {
        // Check if user is admin
        String role = (String) session.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return "redirect:/catalog";
        }
        return "promote-user";
    }
    
    @PostMapping("/admin/promote-user/search")
    public String searchUser(
            @RequestParam("identifier") String identifier,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        // Check if user is admin
        String role = (String) session.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return "redirect:/catalog";
        }
        
        // Trim the identifier
        identifier = identifier.trim();
        
        if (identifier.isEmpty()) {
            redirectAttributes.addFlashAttribute("searchError", "Please enter a username or email");
            return "redirect:/promote-user";
        }
        
        // Try to find user by username first, then by email
        Optional<User> userOpt = userRepository.findByUsername(identifier);
        if (!userOpt.isPresent()) {
            userOpt = userRepository.findByEmail(identifier);
        }
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            redirectAttributes.addFlashAttribute("promoteCandidate", user);
            redirectAttributes.addFlashAttribute("identifier", identifier);
            
            // Check if user is already an admin
            if ("ADMIN".equals(user.getRole())) {
                redirectAttributes.addFlashAttribute("searchError", "This user is already an admin");
            }
        } else {
            redirectAttributes.addFlashAttribute("searchError", "User does not exist");
            redirectAttributes.addFlashAttribute("identifier", identifier);
        }
        
        return "redirect:/promote-user";
    }
    
    @PostMapping("/admin/promote-user/promote")
    public String promoteUser(
            @RequestParam("userId") Long userId,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        // Check if user is admin
        String role = (String) session.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return "redirect:/catalog";
        }
        
        // Find the user to promote
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("searchError", "User not found");
            return "redirect:/promote-user";
        }
        
        User user = userOpt.get();
        
        // Check if user is already an admin
        if ("ADMIN".equals(user.getRole())) {
            redirectAttributes.addFlashAttribute("searchError", "User is already an admin");
            redirectAttributes.addFlashAttribute("promoteCandidate", user);
            return "redirect:/promote-user";
        }
        
        // Promote user to admin
        user.setRole("ADMIN");
        userRepository.save(user);
        
        // Show success message
        redirectAttributes.addFlashAttribute("promotionSuccess", 
            user.getFullName() + " has been promoted to admin");
        redirectAttributes.addFlashAttribute("promoteCandidate", user);
        
        return "redirect:/promote-user";
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        // Invalidate the session to log out the user
        session.invalidate();
        // Redirect to index page after logout
        return "redirect:/index";
    }

}
