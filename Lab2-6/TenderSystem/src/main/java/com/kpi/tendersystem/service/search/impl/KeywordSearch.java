package com.kpi.tendersystem.service.search.impl;

import com.kpi.tendersystem.model.Tender;
import com.kpi.tendersystem.service.search.TenderSearch;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class KeywordSearch implements TenderSearch {
    @Override
    public Collection<Tender> search(Collection<Tender> tenders, String query) {
        // "hello,world;ok;lets,go"
        ArrayList<Tender> result = new ArrayList<>();

        for (String subQuery : query.split(";")) {
            String[] keyWords = subQuery.split(",");
            result.addAll(tenders.stream().filter(tender -> {
                boolean isValid = true;
                for (String keyWord : keyWords) {
                    if (!tender.getTitle().contains(keyWord)) {
                        isValid = false;
                        break;
                    }
                }
                return !(result.contains(tender)) && isValid;
            }).toList());
        }

        return result;
    }
}
