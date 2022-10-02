package com.kpi.tendersystem.model;

import com.kpi.tendersystem.model.auth.User;
import com.kpi.tendersystem.model.form.FormOffer;

import java.io.Serializable;

public class Offer extends FormOffer implements Serializable {
    private Integer id;

    private Tender tender;

    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Tender getTender() {
        return tender;
    }

    public void setTender(Tender tender) {
        this.tender = tender;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
