package com.example.demo.DTO.display;

import com.example.demo.validator.PasswordMatches;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

/**
 *
 */
@Getter
@Setter
@PasswordMatches
public class UserRegistrationDto {
    @NotEmpty( message = "{user.firstName.msg}")
    private String firstName;

    @NotEmpty( message = "{user.lastName.msg}")
    private String lastName;

    @Pattern(regexp = "^[a-zA-Z0-9]{8,32}$", message = "{user.username.msg}")
    private String username;

    @Pattern(regexp = "^[a-z][a-z0-9_\\.]{5,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$",  message = "{user.email.msg}")
    private String email;

    @Size(min = 8, max = 32,  message = "{user.password.msg}")
    private String password;

    @Size(min = 8, max = 32,  message = "{user.password.msg}")
    private String confirmPassword;
}