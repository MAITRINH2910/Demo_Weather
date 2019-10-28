package com.example.demo.controller;

import com.example.demo.entity.UserEntity;
import com.example.demo.service.UserService;
import com.example.demo.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

@Controller
public class AuthController {

    @Autowired
    UserService userService;

    /**
     * Load pageLogin when user access to APP
     *
     * @return
     */
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            @RequestParam(value = "success", required = false) String success,
                            Model model) {
        String message = null;
        if(error != null) {
            message = "Username or Password is incorrect !!";
        }
        if(logout != null) {
            message = "You have been successfully logged out !!";
        }
        if(success !=null){
            message = "Registered Successfully !!";
        }
        model.addAttribute("message", message);
        return "page_auth/login";
    }

    /**
     * Check UserName have been existed
     *
     * @param userName
     * @return True if UserName existed, else return False
     */
    public boolean checkUserExist(String userName) {
        return userService.existsByUserName(userName);
    }

    /**
     * Process default URL login failed
     *
     * @param username
     * @return page Login and message status login
     */
    @PostMapping("/performFailUrl")
    private String processUrlFail(@RequestParam String username) {
        // Check UserName have been exist, if result = true
        if (checkUserExist(username)) {
            // Find UserEntity by userName, Then check status of User
            UserEntity userEntity = userService.findUserByUsername(username);
            // Check status User, If Status = Unactive
            if (userEntity.isActive() == Constants.UN_ACTIVE)
                // Return page 401 , announcement User is block
                return "redirect:/401";
            else
                // Return page login and message login failed
                return "redirect:login?error";
        }
        return "redirect:login?error";
    }

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
        return "redirect:/";
    }

    @PostMapping("/postLogin")
    public String postLogin(Model model) {
        // read principal out of security context and set it to session
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        validatePrinciple(authentication.getPrincipal());
        // read principal out of security context and set it to session
        boolean hasAdminRole = false;

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
                hasAdminRole = true;
                break;
            } else {
                hasAdminRole = false;
                break;
            }
        }
        if (hasAdminRole) {
            return "redirect:/admin";
        } else {
            return "redirect:/";
        }
    }

    private void validatePrinciple(Object principal) {
        if (!(principal instanceof UserDetails)) {
            throw new IllegalArgumentException("Principal can not be null!");
        }
    }

}
