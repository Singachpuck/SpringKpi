package com.kpi.tendersystem.dao;

import com.kpi.tendersystem.model.Offer;

import java.util.Collection;

public interface OfferDao {

    void add(final Offer offer);
    Collection<Offer> getByUsername(final String username);

    Offer getByUsernameAndTenderId(final String username, final int tenderId);

    Collection<Offer> getByTender(final int tenderId);
}
