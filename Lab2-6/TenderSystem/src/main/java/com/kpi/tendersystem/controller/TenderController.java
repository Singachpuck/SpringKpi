package com.kpi.tendersystem.controller;

import com.kpi.tendersystem.model.form.FormOffer;
import com.kpi.tendersystem.model.form.FormTender;
import com.kpi.tendersystem.service.OfferService;
import com.kpi.tendersystem.service.TenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

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

    @GetMapping
    public String showAll(@RequestParam(required = false) String search, final Model model) {
        // Page with active tenders
        model.addAttribute("tenders", tenderService.getAllActive());
        return "tenders";
    }

    @GetMapping(NEW)
    public String create(Model model) {
        model.addAttribute("newTender", new FormTender());
        return "createTender";
    }

    @PostMapping(NEW)
    public String store(Principal principal, @ModelAttribute FormTender formTender) {
        tenderService.addTender(principal.getName(), formTender);
        return "redirect:/tenders";
    }

    @GetMapping(ONE)
    public String show(@PathVariable int tenderId, Model model, Principal principal) {
        model.addAttribute("tender", tenderService.getTender(tenderId));
        model.addAttribute("formOffer", new FormOffer());
        model.addAttribute("patchTender", new FormTender());
        model.addAttribute("oldOffer", offerService.getByUsernameAndTenderId(principal.getName(), tenderId));
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

    @Controller
    @RequestMapping(OfferController.BASE)
    static class OfferController {
        public static final String BASE = TenderController.BASE + TenderController.ONE + "/offers";

        @Autowired
        private OfferService offerService;

        @Autowired
        private TenderService tenderService;

        @PostMapping
        public String store(@PathVariable int tenderId, Principal principal, @ModelAttribute FormOffer formOffer) {
            // Save data from form
            offerService.addOffer(tenderId, principal.getName(), formOffer);
            return "redirect:/tenders/" + tenderId;
        }

        @GetMapping
        public String show(@PathVariable int tenderId, Principal principal, Model model) {
            model.addAttribute("tender", tenderService.getTender(tenderId));
            model.addAttribute("offers", offerService.getAllByTender(principal.getName(), tenderId));
            return "offers";
        }
    }
}
