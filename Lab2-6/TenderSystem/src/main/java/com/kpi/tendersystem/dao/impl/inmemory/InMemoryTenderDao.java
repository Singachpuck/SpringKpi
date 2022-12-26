package com.kpi.tendersystem.dao.impl.inmemory;

import com.kpi.tendersystem.dao.TenderDao;
import com.kpi.tendersystem.dao.impl.inmemory.database.InMemoryDb;
import com.kpi.tendersystem.model.Tender;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class InMemoryTenderDao implements TenderDao {
    private static final Collection<Tender> tenders = InMemoryDb.loadTenders();

    @Override
    public Collection<Tender> getAll(int offset, int limit) {
        final int skipIndex = offset * limit;
        return tenders
                .stream()
                .skip(skipIndex)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Tender> getAll() {
        return new ArrayList<>(tenders);
    }

    @Override
    public Optional<Tender> getById(final int id) {
        return tenders
                .stream()
                .filter(tender -> tender.getId() == id)
                .findFirst();
    }

    @Override
    public int add(Tender tender) {
        if (tender.getId() == null) {
            tender.setId(generateId());
        }
        tenders.add(tender);
        return tender.getId();
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
