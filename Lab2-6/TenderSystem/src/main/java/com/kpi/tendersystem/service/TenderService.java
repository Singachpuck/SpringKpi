package com.kpi.tendersystem.service;

import com.kpi.tendersystem.dao.TenderDao;
import com.kpi.tendersystem.model.Tender;
import com.kpi.tendersystem.model.auth.User;
import com.kpi.tendersystem.model.form.FormTender;
import com.kpi.tendersystem.service.search.TenderSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TenderService {

    @Autowired
    private TenderSearch tenderSearch;

    @Autowired
    private TenderDao tenderDao;

    /**
     *
     * @param searchText filtration query (use ',' to split tokens for one filter group, use ';' to split filter groups)
     * @param offset pagination offset (default 0)
     * @param limit pagination limit (default 14)
     * @return Return collection of tenders with pagination and filtration
     */
    public Collection<Tender> getAll(String searchText, int offset, int limit) {
        final Collection<Tender> activeTenders = tenderDao.getAll(offset, limit);
        return (searchText == null) ? activeTenders : tenderSearch.search(activeTenders, searchText);
    }

    public Collection<Tender> getAllActive(String searchText) {
        final Collection<Tender> activeTenders = tenderDao
                .getAll()
                .stream()
                .filter(Tender::isActive)
                .collect(Collectors.toList());

        return (searchText == null) ? activeTenders : tenderSearch.search(activeTenders, searchText);
    }

    public Optional<Tender> getTenderById(final int id) {
        return tenderDao.getById(id);
    }

    public int addTender(final User user, final FormTender formTender) {
        final Tender newTender = new Tender();
        newTender.setPrice(formTender.getPrice());
        newTender.setTitle(formTender.getTitle());
        newTender.setCategory(formTender.getCategory());
        newTender.setEndDate(formTender.getEndDate());
        newTender.setOwner(user);
        newTender.setActive(true);
        newTender.setStartDate(new Date());
        return tenderDao.add(newTender);
    }

    public void updateTender(final int tenderId, final FormTender formTender) {
        final Tender updateTender = this.getTenderById(tenderId)
                .orElseThrow(() -> new IllegalArgumentException("No tender with id: " + tenderId));
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
