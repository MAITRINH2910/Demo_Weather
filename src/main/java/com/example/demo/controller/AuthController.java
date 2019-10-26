package com.example.demo.controller;

import com.example.demo.DTO.display.LoginForm;
import com.example.demo.entity.UserEntity;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    UserService userService;

    /**
     * Load pageLogin when user access to APP
     *
     * @return
     */

    @ModelAttribute("userForm")
    public LoginForm userLoginDto() {
        return new LoginForm();
    }

    @GetMapping("/login")
    public String loginForm(Model model, @RequestParam(value = "error", required = false) String error) {
        LoginForm accountDto = new LoginForm();
        model.addAttribute("userForm", accountDto);
        String errorMessage = null;
        if (error != null) {
            errorMessage = "Username or Password is incorrect !!";
        }
        model.addAttribute("errorMessage", errorMessage);
        return "page_auth/login";
    }

    @PostMapping("/checkUserName")
    @ResponseBody
    public String checkExistsByUserName(@RequestParam String userName) {
        Boolean result = userService.existsByUserName(userName);
        return "" + result;
    }

    @PostMapping("/login")
    public String doLogin(@ModelAttribute("userForm") @Valid LoginForm userForm,
                          BindingResult result) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        UserEntity username = userService.findUserByUsername(userForm.getUsername());

        if (result.hasErrors()) { /* Chưa nhập 1 trong 2 trường hoặc 2 trường và nhấn nút Login*/
            return "page_auth/login";
        } else {
            if (username == null) {
                result.rejectValue("username", null, "No username in our system");
            } else {
                if (userForm.getUsername().equals(username.getUsername())) {
                    if (encoder.matches(userForm.getPassword(), username.getPassword())) {
                        return "user/home";
                    } else {
                        result.rejectValue("password", null, "Wrong password!");
                    }
                }
            }
            return "page_auth/login";
        }
    }
//    @GetMapping("/login")
//    public String login(@RequestParam(value = "error", required = false) String
//                                error, @RequestParam(value = "logout", required = false) String logout, Model model,
//    @ModelAttribute("userForm") @Valid LoginForm userForm
//    ) {
//        String errorMessage = null;
//        if (error != null) {
//            errorMessage = "Username or Password is incorrect !!";
//        } else if (logout != null) {
//            errorMessage = "You have been successfully logged out !!";
//        }
//        model.addAttribute("errorMessage", errorMessage);
//        return "common/login";
//    }

    /**
     * Logout account on APP
     *
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }

}
