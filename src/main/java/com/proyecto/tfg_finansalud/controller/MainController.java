package com.proyecto.tfg_finansalud.controller;

import com.proyecto.tfg_finansalud.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class MainController {
    UserService userService;

    @GetMapping({"/login","/"})
    public String login() {
        return "/user_login/user_login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        return "user_account_registration/user_register";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "/dashboard/dashboard";
    }

    @GetMapping("/navigation-bar")
    public String getNavigationBar() {
        return "/navigation-bar/navigation-bar";
    }

    @GetMapping("/header")
    public String getHeader() {
        return "/header/header";
    }

    @GetMapping("/monthly-overview")
    public String getMonthlyOverview() {
        return "/overview/monthly-overview";
    }

    @GetMapping("/profile")
    public String getProfile() {
        return "/profile/profile";
    }

//    @GetMapping("/image-upload-ocr")
//    public String getImageUpload() {
//        return "/image-upload-ocr/image-upload-ocr";
//    }

    @GetMapping("/market")
    public String getMarket() {
        return "/market/market";
    }


}
