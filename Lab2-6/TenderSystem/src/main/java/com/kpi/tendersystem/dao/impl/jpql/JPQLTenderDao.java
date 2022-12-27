package com.kpi.tendersystem.dao.impl.jpql;

import com.kpi.tendersystem.dao.TenderDao;
import com.kpi.tendersystem.model.Tender;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;
import java.util.Optional;

@Repository
@Transactional
public class JPQLTenderDao implements TenderDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<Tender> getAll(int offset, int limit) {
        final Query query = entityManager.createQuery("select t from Tender t");
        query.setFirstResult(offset * limit);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public Collection<Tender> getAll() {
        final Query query = entityManager.createQuery("select t from Tender t");
        return query.getResultList();
    }

    @Override
    public Optional<Tender> getById(int id) {
        final Query query = entityManager.createQuery("select t from Tender t where t.id=:id");
        query.setParameter("id", id);
        try {
            return Optional.ofNullable((Tender) query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public int add(Tender tender) {
        entityManager.persist(tender);
        entityManager.flush();
        return tender.getId();
    }

    @Override
    public void update(Tender tender) {
        entityManager.merge(tender);
    }

    @Override
    public void delete(int id) {
        final Query query = entityManager.createQuery("select t from Tender t where t.id=:id");
        query.setParameter("id", id);
        final Tender tender = (Tender) query.getSingleResult();
        if (tender != null) {
            entityManager.remove(tender);
        }
    }
}
