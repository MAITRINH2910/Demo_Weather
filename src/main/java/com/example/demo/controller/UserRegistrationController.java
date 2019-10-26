package com.example.demo.controller;

import com.example.demo.DTO.display.UserRegistrationDto;
import com.example.demo.entity.UserEntity;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * This class implement registration new account:
 * 1. Show Registration Form
 * 2. Check Username or Email is exist in Database and save account
 */
@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    /**
     * Show Registration Form when click the link: "/registration"
     *
     * @return
     */
    @GetMapping
    public String showRegistrationForm() {
        return "page_auth/registration";
    }

    /**
     * Check Email Existing or Username Existing
     * Create New Account
     *
     * @param userDto
     * @param result
     * @return
     */
    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto userDto,
                                      BindingResult result) {

        UserEntity emailExisting = userService.findUserByEmail(userDto.getEmail());
        UserEntity usernameExisting = userService.findUserByUsername(userDto.getUsername());
        if (emailExisting != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (usernameExisting != null) {
            result.rejectValue("username", null, "There is already an account registered with that username");
        }
        if (result.hasErrors()) {
            return "page_auth/registration";
        }

        userService.saveUserDto(userDto);
        return "redirect:/login?success";
    }

    @PostMapping("/checkUserName")
    @ResponseBody
    public String checkExistsByUserName(@RequestParam String userName) {
        Boolean result = userService.existsByUserName(userName);
        return "" + result;
    }
}