package com.kpi.tendersystem.dao.impl.jdbc;

import com.kpi.tendersystem.dao.UserDao;
import com.kpi.tendersystem.model.auth.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Repository
@Primary
@Transactional
public class JdbcUserDao implements UserDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private RowMapper<User> resultSetToUser;

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getByUsername(String username) {
        final String query = "SELECT u.id as user_id, u.username, u.password as user_password, u.account_non_expired, u.account_non_locked, u.credentials_non_expired, u.enabled as user_enabled FROM \"user\" u " +
                "WHERE username=:username";
        Map<String, Object> namedParams = Map.of("username", username);
        final User user = jdbcTemplate.queryForObject(query, namedParams, resultSetToUser);
        return Optional.ofNullable(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getById(int id) {
        final String query = "SELECT u.id as user_id, u.username, u.password as user_password, u.account_non_expired, u.account_non_locked, u.credentials_non_expired, u.enabled as user_enabled FROM \"user\" u " +
                "WHERE u.id=:id";
        Map<String, Object> namedParams = Map.of("id", id);
        final User user = jdbcTemplate.queryForObject(query, namedParams, resultSetToUser);
        return Optional.ofNullable(user);
    }
}
