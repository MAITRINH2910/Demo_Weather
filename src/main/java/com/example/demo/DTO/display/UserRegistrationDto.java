package com.example.demo.DTO.display;

import com.example.demo.validator.PasswordMatches;

import javax.validation.constraints.*;

/**
 *
 */
@PasswordMatches
public class UserRegistrationDto {
    @NotEmpty( message = "{user.firstName.msg}")
    private String firstName;

    @NotEmpty( message = "{user.lastName.msg}")
    private String lastName;

    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,32}$", message = "{user.username.msg}")
    private String username;

    @Pattern(regexp = "^[a-z][a-z0-9_\\.]{5,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$",  message = "{user.email.msg}")
    private String email;

    @Size(min = 8, max = 32,  message = "{user.password.msg}")
    private String password;

    @Size(min = 8, max = 32,  message = "{user.password.msg}")
    private String confirmPassword;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}