package com.kpi.tendersystem.dao;

import com.kpi.tendersystem.model.Tender;

import java.util.Collection;
import java.util.Optional;

public interface TenderDao {

    Collection<Tender> getAll(int offset, int limit);

    Collection<Tender> getAll();
    Optional<Tender> getById(final int id);
    Tender add(final Tender tender);

    void update(final Tender tender);
    void delete(final int id);
}
