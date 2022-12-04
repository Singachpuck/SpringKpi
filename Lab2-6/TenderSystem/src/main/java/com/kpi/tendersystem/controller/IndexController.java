package com.kpi.tendersystem.controller;

import com.kpi.tendersystem.controller.exception.GeneralExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    private GeneralExceptionHandler exceptionHandler;

    @GetMapping("/*")
    public ModelAndView notFound() {
        final ResponseStatusException statusException = new ResponseStatusException(HttpStatus.NOT_FOUND, "Page not found");
        return exceptionHandler.generalError(statusException);
    }

    @GetMapping
    public String index() {
        return "redirect:/tenders";
    }
}
