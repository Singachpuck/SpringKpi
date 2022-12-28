package com.kpi.tendersystem.dao.impl.jpql;

import com.kpi.tendersystem.dao.UserDao;
import com.kpi.tendersystem.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Optional;

@Repository
@Transactional
public class JPQLUserDao implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<User> getByUsername(String username) {
        final Query query = entityManager.createQuery("select u from User u where u.username=:username");
        query.setParameter("username", username);
        try {
            return Optional.ofNullable((User) query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> getById(int id) {
        final Query query = entityManager.createQuery("select u from User u where u.id=:id");
        query.setParameter("id", id);
        try {
            return Optional.ofNullable((User) query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
