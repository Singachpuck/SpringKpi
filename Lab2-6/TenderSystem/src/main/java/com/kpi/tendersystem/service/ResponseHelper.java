package com.kpi.tendersystem.service;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ResponseHelper {

    @Autowired
    private Gson gson;

    public Map<String, Object> composeError(HttpStatus status, String description) {
        final Map<String, Object> error = new LinkedHashMap<>();
        error.put("type", "error");
        error.put("status", status.value());
        error.put("description", description);
        return error;
    }

    public String composeErrorJson(HttpStatus status, String description) {
        final Map<String, Object> error = this.composeError(status, description);
        return gson.toJson(error);
    }
}
