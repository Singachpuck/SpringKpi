package com.kpi.tendersystem.controller.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ModelAndView generalError(ResponseStatusException e) {
        final ModelAndView mav = new ModelAndView();
        mav.setStatus(e.getStatus());
        mav.addObject("exception", e);
        mav.setViewName("error/errorHandler");
        return mav;
    }
}
