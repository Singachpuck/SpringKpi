package com.kpi.tendersystem.service;

import com.kpi.tendersystem.dao.OfferDao;
import com.kpi.tendersystem.dao.TenderDao;
import com.kpi.tendersystem.dao.UserDao;
import com.kpi.tendersystem.model.Offer;
import com.kpi.tendersystem.model.form.FormOffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class OfferService {

    @Autowired
    private UserService userService;

    @Autowired
    private TenderService tenderService;

    @Autowired
    private OfferDao offerDao;

    public void addOffer(final int tenderId, final String username, final FormOffer formOffer) {
        if (getByUsernameAndTenderId(username, tenderId) != null) {
            throw new IllegalStateException("Offer for tender " + tenderId + " from user " + username + " already exists");
        }
        final Offer newOffer = new Offer();
        try {
            newOffer.setTender(tenderService.getTender(tenderId));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tender " + tenderId + " doesn't exist");
        }
        newOffer.setUser(userService.getByUsername(username));
        newOffer.setDescription(formOffer.getDescription());
        offerDao.add(newOffer);
    }

    public Collection<Offer> getByUsername(final String username) {
        return offerDao.getByUsername(username);
    }

    public Offer getByUsernameAndTenderId(final String username, final int tenderId) {
        return offerDao.getByUsernameAndTenderId(username, tenderId);
    }

    public Collection<Offer> getAllByTender(final String username, final int tenderId) {
        if (!Objects.equals(tenderService.getTender(tenderId).getOwner().getUsername(), username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Resource is not allowed!");
        }
        return offerDao.getByTender(tenderId);
    }

}
