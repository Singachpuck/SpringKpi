package com.kpi.tendersystem.dao;

import com.kpi.tendersystem.model.User;

import java.util.Optional;

public interface UserDao {

    Optional<User> getByUsername(final String username);

    Optional<User> getById(final int id);
}
