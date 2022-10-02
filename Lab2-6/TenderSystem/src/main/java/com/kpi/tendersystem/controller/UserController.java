package com.kpi.tendersystem.controller;

import com.kpi.tendersystem.service.OfferService;
import com.kpi.tendersystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OfferService offerService;

    @GetMapping("/profile")
    public String index(Principal principal, Model model) {
        model.addAttribute("user", userService.getByUsername(principal.getName()));
        model.addAttribute("offers", offerService.getByUsername(principal.getName()));
        return "profile";
    }
}
