package com.proyecto.tfg_finansalud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {
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
