package com.kpi.tendersystem.controller;

import com.kpi.tendersystem.model.Offer;
import com.kpi.tendersystem.model.Tender;
import com.kpi.tendersystem.model.auth.User;
import com.kpi.tendersystem.model.form.FormOffer;
import com.kpi.tendersystem.model.form.FormTender;
import com.kpi.tendersystem.service.OfferService;
import com.kpi.tendersystem.service.TenderService;
import com.kpi.tendersystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping(TenderController.BASE)
public class TenderController {

    public static final String BASE = "/tenders";

    public static final String NEW = "/create";

    public static final String ONE = "/{tenderId}";

    @Autowired
    private TenderService tenderService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String showAll(@RequestParam(required = false) String search, final Model model) {
        // Page with active tenders
        model.addAttribute("tenders", tenderService.getAllActive(search));
        return "tenders";
    }

    @GetMapping(NEW)
    public String create(Model model) {
        model.addAttribute("newTender", new FormTender());
        return "createTender";
    }

    @PostMapping(NEW)
    public String store(Principal principal, @ModelAttribute FormTender formTender) {
        final String username = principal.getName();
        final User user = userService
                .getByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can not resolve user"));

        final int tenderId = tenderService.addTender(user, formTender);

        return "redirect:/tenders/" + tenderId;
    }

    @GetMapping(ONE)
    public String show(@PathVariable int tenderId, Model model, Principal principal) {
        final Optional<Tender> tender = tenderService.getTenderById(tenderId);
        if (tender.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Page not found");
        }
        final Offer offer = offerService.getByUsernameAndTenderId(principal.getName(), tenderId).orElse(null);
        model.addAttribute("tender", tender.get());
        model.addAttribute("formOffer", new FormOffer());
        model.addAttribute("patchTender", new FormTender());
        model.addAttribute("oldOffer", offer);
        model.addAttribute("username", principal.getName());
        return "tender";
    }

    @DeleteMapping(ONE)
    public String delete(@PathVariable int tenderId) {
        tenderService.deleteTender(tenderId);
        return "redirect:/tenders";
    }

    @PutMapping(ONE)
    public String update(@PathVariable int tenderId, @ModelAttribute FormTender tender, HttpServletRequest request) {
        tenderService.updateTender(tenderId, tender);
        return "redirect:" + request.getRequestURI();
    }
}
