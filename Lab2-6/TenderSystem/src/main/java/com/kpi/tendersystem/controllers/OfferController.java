package com.kpi.tendersystem.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class OfferController {

    @GetMapping("/tenders/{tenderId}/offers")
    public String index(@PathVariable String tenderId) {
        // Page with offers of the tender
        return "";
    }

    @GetMapping("/tenders/{tenderId}/offers/create")
    public String create(@PathVariable String tenderId) {
        // Page with create offer form
        return "";
    }

    @PutMapping("/tenders/{tenderId}/offers/create")
    public String store(@PathVariable String tenderId) {
        // Save data from form
        return "";
    }

    @GetMapping("/tenders/{tenderId}/offers/{offerId}")
    public String show(@PathVariable String tenderId, @PathVariable String offerId) {
        // Page to show an offer
        return "";
    }

    @GetMapping("/tenders/{tenderId}/offers/{offerId}/edit")
    public String edit(@PathVariable String tenderId, @PathVariable String offerId) {
        // Page with edit form
        return "";
    }

    @PatchMapping("/tenders/{tenderId}/offers/{offerId}/edit")
    public String update(@PathVariable String tenderId, @PathVariable String offerId) {
        // Page with edit form
        return "";
    }

    @DeleteMapping("/tenders/{tenderId}/offers/{offerId}")
    public String destroy(@PathVariable String tenderId, @PathVariable String offerId) {
        // Delete offer by id
        return "";
    }
}
