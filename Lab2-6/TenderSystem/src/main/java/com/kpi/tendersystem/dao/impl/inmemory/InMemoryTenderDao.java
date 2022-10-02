package com.kpi.tendersystem.dao.impl.inmemory;

import com.kpi.tendersystem.dao.TenderDao;
import com.kpi.tendersystem.dao.impl.database.InMemoryDb;
import com.kpi.tendersystem.model.Offer;
import com.kpi.tendersystem.model.Tender;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryTenderDao implements TenderDao {
    private static final Collection<Tender> tenders = InMemoryDb.loadTenders();

    @Override
    public Collection<Tender> getAll() {
        return new ArrayList<>(tenders);
    }

    @Override
    public Tender get(final int id) {
        return tenders
                .stream()
                .filter(tender -> tender.getId() == id)
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public void add(Tender tender) {
        if (tender.getId() == null) {
            tender.setId(generateId());
        }
        tenders.add(tender);
    }

    @Override
    public void update(Tender tender) {
        delete(tender.getId());
        add(tender);
    }

    @Override
    public void delete(int id) {
        tenders.removeIf(tender -> tender.getId() == id);
    }

    private int generateId() {
        return tenders
                .stream()
                .map(Tender::getId)
                .max(Comparator.comparingInt(i -> i))
                .orElse(0) + 1;
    }
}
