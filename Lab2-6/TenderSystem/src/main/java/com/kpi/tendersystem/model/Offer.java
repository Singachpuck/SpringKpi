package com.kpi.tendersystem.model;

public class Offer {
    private int id;
    private Tender tender;
    private double price;

    public int getId() {
        return id;
    }

    public Tender getTender() {
        return tender;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
