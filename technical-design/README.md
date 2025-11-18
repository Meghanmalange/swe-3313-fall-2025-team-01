# Technical Design


<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li><a href="#implementation-language">Implementation Language</a></li>
    <li><a href="#implementation-framework">Implementation Framework</a></li>
    <li><a href="#data-storage-plan">Data Storage Plan</a></li>
    <li><a href="#entity-relationship-diagram">Entity Relationship Diagram</a></li>
    <li><a href="#entity/field-descriptions">Entity/Field Descriptions</a></li>
    <li><a href="#data-examples">Data Examples</a></li>
    <li><a href="#database-seed-data">Database Seed Data</a></li>
    <li><a href="#authentication-and-authorization-plan">Authentication and Authorization Plan</a></li>
    <li><a href="#coding-style-guide">Coding Style Guide</a></li>
    <li><a href="#technical-design-presentation">Technical Design Presentation</a></li>
  </ol>
</details>


<a id="implementation-language"></a>
# :book: Implementation Language



<p align="right">(<a href="#readme-top">back to top</a>)</p>


<a id="#implementation-framework"></a>
# :atom_symbol: Implementation Framework



<p align="right">(<a href="#readme-top">back to top</a>)</p>


<a id="data-storage-plan"></a>
# :people_holding_hands: Data Storage Plan


  
<p align="right">(<a href="#readme-top">back to top</a>)</p>


<a id="entity-relationship-diagram"></a>
# :link: Entity Relationship Diagram

- To build our application, we will be coding in Java, utilizing the Spring Boot framework. We will use SQLite as our database to store user and item attributes.
- For a detailed explanation of our tool choices and reasoning for them, click [here](./project-plan/technology-selection/README.md/)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<a id="entity/field-descriptions"></a>
# :book: Entity/Field Descriptions

Welcome to the project repository for **Team 01 - The African Royals**. Here you will find all of the artifacts, presentations, documentation, and source code for our SWE 3313 class project.

We will create a small e-commerce website using Java, SpringBoot, and SQLite to sell one-of-a-kind African jewelry.

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<a id="#data-examples"></a>
# :atom_symbol: Data Examples



<p align="right">(<a href="#readme-top">back to top</a>)</p>


<a id="database-seed-data"></a>
# :people_holding_hands: Database Seed Data


  
<p align="right">(<a href="#readme-top">back to top</a>)</p>


<a id="authentication-and-authorization-plan"></a>
# :link: Authentication and Authorization Plan

## 1. Overall Approach
We’ll use role-based access control (RBAC) with a single User model and a Role concept:
- Every authenticated person is a User.
- An Administrator is just a User with role = ADMIN.
- There is one login screen; after login, the system inspects the user’s role to decide:
    • which UI elements to show (e.g., “Sales Report”, “Promote User”),
    • which backend endpoints they’re allowed to call.
  
**Technologies:**
Backend: Java + Spring Boot
Persistence: Spring Data JPA + SQLite
Security: Spring Security (or a light custom equivalent), HTTP session-based for V1

## 2. Data Model
### 2.1 USER Table

| Column              | Type             |   Notes                                    |
| ------------------- | ---------------- | -----------------------------------------  |
| Userid              | INTEGER PK       | Auto_Increment primary key                 |
| Username            | TEXT UNIQUE      | Required, Unique                           |
| email               | BOOLEAN          | For account reference    |
| Full name           | TEXT             | User's full name    |
| password_hash       | TEXT             | BCrypt hash of password (min 6 chars)      |
| role                | TEXT             | 'USER' or 'ADMIN'                          |
| created_at          | DATETIME         | Creation Timestamp                         |
| email               | BOOLEAN          | For future account disabling (optional)    |

### 2.2 Java Domain Model
```sh
public enum Role {
    USER,
    ADMIN
}

@Entity
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER; // default

    // constructors, getters, setters

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }
}
```

Initial admins are seeded via an SQL script or data initializer instead of the registration screen to satisfy the "Admins cannot self-register" requirement

## 3. Authentication Flow

### 3.1 Registration (T1S-1: Register a New User)
Requirement:
```sh
   “The user must be able to self-register… Admins cannot self-register.”
```

Implementation:
- Endpoint: POST /auth/register
- Request body: { username, password, confirmPassword }
- Validation:
    - username is unique
    - password length ≥ 6
- The role is not accepted from client; it is forced to Role.USER on server.
```sh
public User registerUser(RegisterRequest req) {
    if (userRepository.existsByUsername(req.getUsername())) {
        throw new DuplicateUsernameException();
    }
    if (req.getPassword().length() < 6) {
        throw new WeakPasswordException();
    }

    User user = new User();
    user.setUsername(req.getUsername());
    user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
    user.setRole(Role.USER); // enforce regular user

    return userRepository.save(user);
}

```

### 3.2 Login (T1S-2: Log in as a Registered User)
Requirement:
```sh
“The user must be able to log in using their registered credentials… direct the user to the main inventory screen upon success.”
```

Implementation:
- Single login screen: POST /auth/login
- Spring Security (or custom) validates credentials.
- On success:
    - Populate SecurityContext / HttpSession with userId and role.
    - Redirect to /inventory (jewelry list).
      
Example(Custom service):
```sh
public User authenticate(String username, String rawPassword) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(BadCredentialsException::new);

    if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
        throw new BadCredentialsException();
    }
    return user;
}

```

Controller:
```sh
@PostMapping("/auth/login")
public String login(@ModelAttribute LoginRequest req, HttpSession session) {
    User user = authService.authenticate(req.getUsername(), req.getPassword());
    session.setAttribute("userId", user.getId());
    session.setAttribute("role", user.getRole());
    return "redirect:/inventory";
}

```

No separate admin login page; same endpoint, different behavior once logged in.

## 4. Authorization Design (What Each Role Can Do)

Regular User can:  
view inventory, search by name, add to cart, checkout, see their receipt 
Admin can additionally: 
log in as admin and promote a regular user to admin (T1S-9) 
view sales report (T1S-11) and individual receipts (T1S-12)

### 4.1 Backend Authorization
We enforce role checks in the controller/service layer.
If using Spring Security:
```sh
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
          .authorizeHttpRequests(auth -> auth
             .requestMatchers("/auth/**", "/static/**").permitAll()
             .requestMatchers("/admin/**").hasRole("ADMIN")
             .requestMatchers("/inventory/**", "/cart/**", "/orders/**").hasRole("USER")
             .anyRequest().authenticated()
          )
          .formLogin(form -> form
             .loginPage("/login")
             .defaultSuccessUrl("/inventory", true)
          );

        return http.build();
    }
}
```

### 4.2 UI-Level Authorization
- After login, front-end fetches /auth/me returning { username, role }.
- Navigation bar conditionally renders:
    - If role == USER → show: Inventory, Cart, Checkout.
    - If role == ADMIN → show: Inventory, Cart + Admin Dashboard, Sales Report, User Management.

This ensures the actions presented to each logged-in user are aligned with their authorization.

## 5. Admin Promotion (T1S-9)
Requirement:
```sh
“Only existing admins can log in and transform a regular user into an admin.”
We’ll implement a simple Admin User Management use case:
```

### 5.1 Endpoint
- GET /admin/users – list users (ADMIN only)
- POST /admin/users/{id}/promote – change role USER→ADMIN
- Service:
```sh
public void promoteToAdmin(Long targetUserId, User actingAdmin) {
    if (!actingAdmin.isAdmin()) {
        throw new AccessDeniedException("Only admins can promote users");
    }

    User target = userRepository.findById(targetUserId)
        .orElseThrow(UserNotFoundException::new);

    if (target.isAdmin()) {
        return; // already admin
    }

    target.setRole(Role.ADMIN);
    userRepository.save(target);
}
```

Controller example:
```sh
@PostMapping("/admin/users/{id}/promote")
public String promote(@PathVariable Long id, HttpSession session) {
    Long actingId = (Long) session.getAttribute("userId");
    User acting = userService.getById(actingId);

    adminService.promoteToAdmin(id, acting);
    return "redirect:/admin/users";
}
```

This directly implements the “transform regular user into admin” requirement.

## 7. Summary

- Authentication
    - Single login UI (/auth/login) for both Users and Admins.
    - Registration (/auth/register) always creates role = USER.
    - Passwords stored as BCrypt hashes.
    - Session stores userId and role.
      
- Authorization
    - Enforced both in backend (role checks / @PreAuthorize / URL mapping) and frontend (conditional rendering).
    - Role.USER:
        - View inventory, search, add to cart, checkout, see receipts.
    - Role.ADMIN:
        - All user actions plus:
        - View sales report.
        - View individual receipts.
        - Promote other users to admin.

- Admin is just a User with role = ADMIN
    - No second login form.
    - Same User entity and users table.
    - Role field and helper methods (isAdmin()) drive access decisions.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<a id="coding-style-guide"></a>
# :people_holding_hands: Coding Style Guide


  
<p align="right">(<a href="#readme-top">back to top</a>)</p>


<a id="technical-design-presentation"></a>
# :link: Technical Design Presentation



<p align="right">(<a href="#readme-top">back to top</a>)</p>
