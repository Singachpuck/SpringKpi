package com.kpi.tendersystem.dao.impl.inmemory;

import com.kpi.tendersystem.dao.UserDao;
import com.kpi.tendersystem.dao.impl.database.InMemoryDb;
import com.kpi.tendersystem.model.auth.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Repository
public class InMemoryUserDao implements UserDao {

    private static final Collection<User> users = InMemoryDb.loadUsers();

    @Override
    public Collection<User> getAll() {
        return new ArrayList<>(users);
    }

    @Override
    public User getByUsername(final String username) {
        return users
                .stream()
                .filter(user -> Objects.equals(user.getUsername(), username))
                .findFirst()
                .orElseThrow(NoSuchFieldError::new);
    }
}
