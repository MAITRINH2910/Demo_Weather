package com.example.demo.controller;

import com.example.demo.entity.UserEntity;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The AdminController class is used to illustrate:
 * 1. Handle Business With Role Admin:
 * 2. Table List Users
 * 3. Active User
 * 4. Change Role User
 * 5. Delete User
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    /**
     * Dashboard Admin Management
     *
     * @param model
     * @return
     */
    @GetMapping
    private String listUsers(Model model) {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userService.findUserByUsername(authUser.getName());
        model.addAttribute("user", user);
        List<UserEntity> users = userService.findAllUser(user.getId());
        model.addAttribute("listUsers", users);
        return "page_admin/table";
    }

    /**
     * Delete User
     *
     * @param id
     * @return
     */
    @PostMapping("/delete")
    private String deleteUser(@RequestParam long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    /**
     * Edit Status Of User (Active <--> Un Active)
     *
     * @param id
     * @return
     */
    @PostMapping("/edit-status-user")
    private String editStatusUser(@RequestParam long id) {
        userService.editStatusUser(id);
        return "redirect:/admin";
    }

    /**
     * Change Role Of User (ADMIN <--> USER)
     *
     * @param id
     * @param role
     */
    @PostMapping("/change-role")
    private void changeRoleUser(@RequestParam long id, @RequestParam String role) {
        userService.editRoleUser(id, role);
    }
}
