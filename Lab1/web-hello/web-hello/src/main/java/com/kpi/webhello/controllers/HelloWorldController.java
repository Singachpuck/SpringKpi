package com.kpi.webhello.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloWorldController {

    @GetMapping("/")
    @ResponseBody
    public String hello() {
        return "Hello world!";
    }

    @GetMapping("/group")
    public String groupInfo() {
        return "group-info.html";
    }

    @GetMapping("/moto")
    public String moto() {
        return "motivation.html";
    }
}
