package com.kpi.tendersystem.controller;

import com.kpi.tendersystem.model.Tender;
import com.kpi.tendersystem.model.User;
import com.kpi.tendersystem.model.form.FormOffer;
import com.kpi.tendersystem.service.OfferService;
import com.kpi.tendersystem.service.TenderService;
import com.kpi.tendersystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Objects;

@Controller
@RequestMapping(OfferController.BASE)
class OfferController {
    public static final String BASE = TenderController.BASE + TenderController.ONE + "/offers";

    @Autowired
    private OfferService offerService;

    @Autowired
    private TenderService tenderService;

    @Autowired
    private UserService userService;

    @PostMapping
    public String store(@PathVariable int tenderId, Principal principal, @ModelAttribute FormOffer formOffer) {
        final String username = principal.getName();
        offerService.getByUsernameAndTenderId(username, tenderId)
                .ifPresent((item) -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Offer for tender " + tenderId + " from user " + username + " already exists");
                });
        final Tender tender = tenderService.getTenderById(tenderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No tender with id: " + tenderId));
        final User user = userService.getByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can not resolve user"));
        offerService.addOffer(tender, user, formOffer);

        return "redirect:/tenders/" + tenderId;
    }

    @GetMapping
    public String show(@PathVariable int tenderId, Principal principal, Model model) {
        final String username = principal.getName();
        final Tender tender = tenderService.getTenderById(tenderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No tender with id: " + tenderId));
        if (!Objects.equals(tender.getOwner().getUsername(), username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User: " + username + " is not an owner of tender: " + tender);
        }
        model.addAttribute("tender", tender);
        model.addAttribute("offers", offerService.getAllByTender(username, tenderId));
        return "offers";
    }
}
