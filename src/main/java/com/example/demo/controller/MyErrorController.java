package com.example.demo.controller;

import com.example.demo.entity.UserEntity;
import com.example.demo.service.UserService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MyErrorController implements ErrorController {
    @Autowired
    UserService userService;

    /**
     * PAGE 401 - Disable Account
     *
     * @return
     */
    @GetMapping("/401")
    private ModelAndView disableAccount() {
        ModelAndView model = new ModelAndView();
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userService.findUserByUsername(authUser.getName());
        model.setViewName("page_error/401");
        return model;
    }

    /**
     * PAGE 403 - User Attempt to Access Page Admin
     *
     * @return
     */
    @GetMapping("/403")
    public ModelAndView accesssDenied() {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userService.findUserByUsername(authUser.getName());
        ModelAndView model = new ModelAndView();

        if (user != null) {
            model.addObject("msg", "Hi " + user.getFirstName()
                    + ", you do not have permission to access this page!");
        } else {
            model.addObject("msg",
                    "You do not have permission to access this page!");
        }
        model.setViewName("page_error/403");
        return model;
    }

    @GetMapping("/error")
    private ModelAndView handleError(HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();

        if(response.getStatus() == HttpStatus.NOT_FOUND.value()) {
            modelAndView.setViewName("page_error/404");
        }
        else if(response.getStatus() == HttpStatus.FORBIDDEN.value()) {
            modelAndView.setViewName("page_error/403");
        }
        else if(response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            modelAndView.setViewName("page_error/500");
        }
        else {
            modelAndView.setViewName("error");
        }

        return modelAndView;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}