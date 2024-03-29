package com.example.demo.DTO.display;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class PasswordForgotDto {

    @Email
    @NotEmpty
    private String email;
}