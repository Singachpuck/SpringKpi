package com.kpi.tendersystem.controller;

import com.kpi.tendersystem.model.User;
import com.kpi.tendersystem.service.OfferService;
import com.kpi.tendersystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;

    private final OfferService offerService;

    @Autowired
    public UserController(UserService userService, OfferService offerService) {
        this.userService = userService;
        this.offerService = offerService;
    }

    @GetMapping("/profile")
    public String index(Principal principal, Model model) {
        final Optional<User> user = userService.getByUsername(principal.getName());
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can not resolve user");
        }
        model.addAttribute("user", user.get());
        model.addAttribute("offers", offerService.getByUsername(principal.getName()));
        return "profile";
    }
}
