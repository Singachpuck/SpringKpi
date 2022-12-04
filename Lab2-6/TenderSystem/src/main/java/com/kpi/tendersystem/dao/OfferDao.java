package com.kpi.tendersystem.dao;

import com.kpi.tendersystem.model.Offer;

import java.util.Collection;
import java.util.Optional;

public interface OfferDao {

    int add(final Offer offer);
    Collection<Offer> getByUsername(final String username);

    Optional<Offer> getByUsernameAndTenderId(final String username, final int tenderId);

    Collection<Offer> getByTender(final int tenderId);

    Optional<Offer> getById(int id);
}
