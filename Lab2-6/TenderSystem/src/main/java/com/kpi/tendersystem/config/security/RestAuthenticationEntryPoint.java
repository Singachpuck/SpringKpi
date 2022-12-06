package com.kpi.tendersystem.config.security;

import com.kpi.tendersystem.service.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ResponseHelper helper;

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException {
        response.addHeader("WWW-Authenticate", "Basic realm=\"Authenticate\"");
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        try (final Writer writer = response.getWriter()) {
            writer.write(helper.composeErrorJson(HttpStatus.UNAUTHORIZED, authenticationException.getMessage()));
        }
    }
}