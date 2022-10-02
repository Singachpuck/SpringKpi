package com.kpi.tendersystem.dao;

import com.kpi.tendersystem.model.Tender;

import java.util.Collection;

public interface TenderDao {

    Collection<Tender> getAll();
    Tender get(final int id);
    void add(final Tender tender);

    void update(final Tender tender);
    void delete(final int id);
}
