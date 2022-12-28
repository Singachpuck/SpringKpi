package com.kpi.tendersystem.dao.impl.jpa;

import com.kpi.tendersystem.dao.UserDao;
import com.kpi.tendersystem.model.User;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Primary
public interface JpaUserDao extends UserDao, JpaRepository<User, Integer> {

    Optional<User> findFirstByUsername(final String username);

    @Override
    @Query("select u from User u where u.username=?1")
    Optional<User> getByUsername(final String username);

    @Override
    @Query("select u from User u where u.id=?1")
    Optional<User> getById(final int id);
}
