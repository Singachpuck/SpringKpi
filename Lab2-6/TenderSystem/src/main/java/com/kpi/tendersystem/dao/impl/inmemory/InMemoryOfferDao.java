package com.kpi.tendersystem.dao.impl.inmemory;

import com.kpi.tendersystem.dao.OfferDao;
import com.kpi.tendersystem.dao.impl.database.InMemoryDb;
import com.kpi.tendersystem.model.Offer;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class InMemoryOfferDao implements OfferDao {

    private static final Collection<Offer> offers = InMemoryDb.loadOffers();

    @Override
    public int add(final Offer offer) {
        if (offer.getId() == null) {
            offer.setId(generateId());
        }

        offers.add(offer);
        return offer.getId();
    }

    @Override
    public Collection<Offer> getByUsername(String username) {
        return offers
                .stream()
                .filter(offer -> Objects.equals(offer.getUser().getUsername(), username))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Offer> getByUsernameAndTenderId(String username, int tenderId) {
        return offers
                .stream()
                .filter(offer -> Objects.equals(offer.getUser().getUsername(), username)
                        && Objects.equals(offer.getTender().getId(), tenderId))
                .findFirst();
    }

    @Override
    public Collection<Offer> getByTender(int tenderId) {
        return offers
                .stream()
                .filter(offer -> Objects.equals(offer.getTender().getId(), tenderId))
                .collect(Collectors.toList());
    }

    private int generateId() {
        return offers
                .stream()
                .map(Offer::getId)
                .max(Comparator.comparingInt(i -> i))
                .orElse(0) + 1;
    }

    @Override
    public Optional<Offer> getById(int id) {
        return offers
                .stream()
                .filter(offer -> offer.getId() == id)
                .findFirst();
    }
}
