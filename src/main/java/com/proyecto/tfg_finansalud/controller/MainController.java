package com.proyecto.tfg_finansalud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {
    @GetMapping({"/login","/"})
    public String login() {
        return "/user_login/user_login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "/dashboard/dashboard";
    }

}
