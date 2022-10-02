package com.kpi.tendersystem.service;

import com.kpi.tendersystem.dao.TenderDao;
import com.kpi.tendersystem.model.Tender;
import com.kpi.tendersystem.model.auth.User;
import com.kpi.tendersystem.model.form.FormTender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TenderService {

    @Autowired
    private UserService userService;

    @Autowired
    private TenderDao tenderDao;

    public Collection<Tender> getAllActive() {
        return tenderDao
                .getAll()
                .stream()
                .filter(Tender::isActive)
                .collect(Collectors.toList());
    }

    public Tender getTender(final int id) throws ResponseStatusException {
        try {
            return tenderDao.get(id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tender with id: " + id + "does not exist");
        }
    }

    public void addTender(final String username, final FormTender formTender) {
        final User owner = userService.getByUsername(username);
        final Tender newTender = new Tender();
        newTender.setPrice(formTender.getPrice());
        newTender.setTitle(formTender.getTitle());
        newTender.setCategory(formTender.getCategory());
        newTender.setEndDate(formTender.getEndDate());
        newTender.setOwner(owner);
        newTender.setActive(true);
        newTender.setStartDate(new Date());
        tenderDao.add(newTender);
    }

    public void updateTender(final int tenderId, final FormTender formTender) {
        final Tender updateTender = getTender(tenderId);
        updateTender.setTitle(formTender.getTitle());
        updateTender.setCategory(formTender.getCategory());
        updateTender.setPrice(formTender.getPrice());
        updateTender.setActive(formTender.isActive());
        updateTender.setEndDate(formTender.getEndDate());
        tenderDao.update(updateTender);
    }

    public void deleteTender(final int tenderId) {
        tenderDao.delete(tenderId);
    }
}
