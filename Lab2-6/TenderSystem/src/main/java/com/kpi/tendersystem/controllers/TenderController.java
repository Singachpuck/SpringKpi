package com.kpi.tendersystem.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class TenderController {

    @GetMapping("/tenders")
    public String index(@RequestParam(required = false) String search) {
        // Page with active tenders
        return "";
    }

    @GetMapping("/tenders/create")
    public String create() {
        // Page with create form
        return "";
    }

    @PutMapping("/tenders/create")
    public String store() {
        // Save data from form
        return "";
    }

    @GetMapping("/tenders/{tenderId}")
    public String show(@PathVariable String tenderId) {
        // Page to show a tender
        return "";
    }

    @GetMapping("/tenders/{tenderId}/edit")
    public String edit(@PathVariable String tenderId) {
        // Page with edit form
        return "";
    }

    @PatchMapping("/tenders/{tenderId}/edit")
    public String update(@PathVariable String tenderId) {
        // Update with data from form
        return "";
    }

    @DeleteMapping("/tenders/{tenderId}")
    public String destroy(@PathVariable String tenderId) {
        // Delete tender by id
        return "";
    }
}
