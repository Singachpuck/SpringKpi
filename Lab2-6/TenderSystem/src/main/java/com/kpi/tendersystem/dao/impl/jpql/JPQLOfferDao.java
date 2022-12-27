package com.kpi.tendersystem.dao.impl.jpql;

import com.kpi.tendersystem.dao.OfferDao;
import com.kpi.tendersystem.model.Offer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Collection;
import java.util.Optional;

@Repository
@Transactional
public class JPQLOfferDao implements OfferDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int add(Offer offer) {
        entityManager.persist(offer);
        entityManager.flush();
        return offer.getId();
    }

    @Override
    public Collection<Offer> getByUsername(String username) {
        final Query query =  entityManager.createNamedQuery(Offer.SELECT_ALL_BY_USERNAME);
        query.setParameter("username", username);
        return query.getResultList();
    }

    @Override
    public Optional<Offer> getByUsernameAndTenderId(String username, int tenderId) {
        final Query query =  entityManager.createNamedQuery(Offer.SELECT_BY_USERNAME_AND_TENDER_ID);
        query.setParameter("username", username);
        query.setParameter("tenderId", tenderId);
        try {
            return Optional.ofNullable((Offer) query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Collection<Offer> getByTender(int tenderId) {
        final Query query =  entityManager.createNamedQuery(Offer.SELECT_BY_TENDER_ID);
        query.setParameter("id", tenderId);
        return query.getResultList();
    }

    @Override
    public Optional<Offer> getById(int id) {
        final Query query =  entityManager.createNamedQuery(Offer.SELECT_BY_ID);
        query.setParameter("id", id);
        try {
            return Optional.ofNullable((Offer) query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
