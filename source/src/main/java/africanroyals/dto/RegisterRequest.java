package africanroyals.dto;

import jakarta.validation.constraints.*;

public class RegisterRequest {

    @NotBlank(message = "Full name is required")
    @Pattern(
        regexp = "^[A-Za-z ]+$",
        message = "Full name can only contain letters and spaces"
    )
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Username is required")
    @Size(min = 6, message = "Username must be at least 6 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    // Getters and Setters
}


