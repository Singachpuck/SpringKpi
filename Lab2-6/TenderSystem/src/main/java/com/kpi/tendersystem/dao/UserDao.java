package com.kpi.tendersystem.dao;

import com.kpi.tendersystem.model.auth.User;

import java.util.Collection;

public interface UserDao {

    Collection<User> getAll();

    User getByUsername(final String username);
}
