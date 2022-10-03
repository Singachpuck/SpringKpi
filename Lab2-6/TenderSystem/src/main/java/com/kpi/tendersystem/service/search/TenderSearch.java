package com.kpi.tendersystem.service.search;

import com.kpi.tendersystem.model.Tender;

import java.util.Collection;

public interface TenderSearch {
    Collection<Tender> search(Collection<Tender> tenders, String query);
}
