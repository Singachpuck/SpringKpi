package com.kpi.tendersystem.service;

import com.kpi.tendersystem.dao.OfferDao;
import com.kpi.tendersystem.model.Offer;
import com.kpi.tendersystem.model.Tender;
import com.kpi.tendersystem.model.auth.User;
import com.kpi.tendersystem.model.form.FormOffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Service
public class OfferService {

    @Autowired
    private TenderService tenderService;

    @Autowired
    private OfferDao offerDao;

    public int addOffer(final Tender tender, final User user, final FormOffer formOffer) {
        final Offer newOffer = new Offer();
        newOffer.setTender(tender);
        newOffer.setUser(user);
        newOffer.setDescription(formOffer.getDescription());
        return offerDao.add(newOffer);
    }

    public Collection<Offer> getByUsername(final String username) {
        return offerDao.getByUsername(username);
    }

    public Optional<Offer> getByUsernameAndTenderId(final String username, final int tenderId) {
        return offerDao.getByUsernameAndTenderId(username, tenderId);
    }

    public Collection<Offer> getAllByTender(final String username, final int tenderId) {
        final Tender tender = tenderService.getTenderById(tenderId)
                .orElseThrow(() -> new IllegalArgumentException("No tender with id: " + tenderId));
        if (!Objects.equals(tender.getOwner().getUsername(), username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Resource is not allowed!");
        }
        return offerDao.getByTender(tenderId);
    }

    public Optional<Offer> getById(int id) {
        return offerDao.getById(id);
    }
}
