package com.kpi.tendersystem.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @GetMapping("/account")
    public String index() {
        // Page with active tenders
        return "";
    }

    @GetMapping("/account/tenders")
    public String tenders() {
        // Page with create form
        return "";
    }

    @PutMapping("/account/offers")
    public String offers() {
        // Save with data from form
        return "";
    }
}
