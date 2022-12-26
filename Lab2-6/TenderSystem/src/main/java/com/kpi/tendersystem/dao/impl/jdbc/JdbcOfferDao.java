package com.kpi.tendersystem.dao.impl.jdbc;

import com.kpi.tendersystem.dao.OfferDao;
import com.kpi.tendersystem.model.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
@Primary
@Transactional
public class JdbcOfferDao implements OfferDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private RowMapper<Offer> resultSetToOffer;

    @Override
    public int add(Offer offer) {
        final String query = "INSERT INTO offer(tender_id, user_id, description) VALUES (:tender_id, :user_id, :description)";
        final Map<String, Object> namedParams = Map.of("tender_id", offer.getTender().getId(),
                "user_id", offer.getUser().getId(),
                "description", offer.getDescription());
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(query, new MapSqlParameterSource(namedParams), keyHolder, new String[] { "id" });
        return (int) keyHolder.getKey();
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Offer> getByUsername(String username) {
        final String query = "SELECT offer.id, description, user_id, tender_id, " +
                "       start_date as tender_start_date, end_date as tender_end_date, title as tender_title, category as tender_category, is_active as tender_is_active, price as tender_price, u.username, u.password as user_password, u.account_non_expired, u.account_non_locked, u.credentials_non_expired, u.enabled as user_enabled, u2.id as tender_user_id, u2.username as tender_username, u2.password as tender_user_password, u2.account_non_expired as tender_account_non_expired, u2.account_non_locked as tender_account_non_locked, u2.credentials_non_expired as tender_credentials_non_expired, u2.enabled as tender_user_enabled " +
                "FROM offer " +
                "INNER JOIN \"user\" u ON u.id=user_id " +
                "INNER JOIN tender t ON t.id=tender_id " +
                "INNER JOIN \"user\" u2 on u2.id = t.owner_id " +
                "WHERE u.username=:username";
        Map<String, Object> namedParams = Map.of("username", username);
        return jdbcTemplate.query(query, namedParams, resultSetToOffer);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Offer> getByUsernameAndTenderId(String username, int tenderId) {
        final String query = "SELECT offer.id, description, user_id, tender_id, " +
                "start_date as tender_start_date, end_date as tender_end_date, title as tender_title, category as tender_category, is_active as tender_is_active, price as tender_price, u.username, u.password as user_password, u.account_non_expired, u.account_non_locked, u.credentials_non_expired, u.enabled as user_enabled, u2.id as tender_user_id, u2.username as tender_username, u2.password as tender_user_password, u2.account_non_expired as tender_account_non_expired, u2.account_non_locked as tender_account_non_locked, u2.credentials_non_expired as tender_credentials_non_expired, u2.enabled as tender_user_enabled " +
                "FROM offer " +
                "INNER JOIN \"user\" u ON u.id=user_id " +
                "INNER JOIN tender t ON t.id=tender_id " +
                "INNER JOIN \"user\" u2 on u2.id = t.owner_id " +
                "WHERE u.username=:username AND tender_id=:tender_id";
        Map<String, Object> namedParams = Map.of("username", username,
                "tender_id", tenderId);
        try {
            final Offer offer = jdbcTemplate.queryForObject(query, namedParams, resultSetToOffer);
            return Optional.ofNullable(offer);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Offer> getByTender(int tenderId) {
        final String query = "SELECT offer.id, description, user_id, tender_id, " +
                "start_date as tender_start_date, end_date as tender_end_date, title as tender_title, category as tender_category, is_active as tender_is_active, price as tender_price, u.username, u.password as user_password, u.account_non_expired, u.account_non_locked, u.credentials_non_expired, u.enabled as user_enabled, u2.id as tender_user_id, u2.username as tender_username, u2.password as tender_user_password, u2.account_non_expired as tender_account_non_expired, u2.account_non_locked as tender_account_non_locked, u2.credentials_non_expired as tender_credentials_non_expired, u2.enabled as tender_user_enabled " +
                "FROM offer " +
                "INNER JOIN \"user\" u ON u.id=user_id " +
                "INNER JOIN tender t ON t.id=tender_id " +
                "INNER JOIN \"user\" u2 on u2.id = t.owner_id " +
                "WHERE tender_id=:tender_id";
        Map<String, Object> namedParams = Map.of("tender_id", tenderId);
        return jdbcTemplate.query(query, namedParams, resultSetToOffer);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Offer> getById(int id) {
        final String query = "SELECT offer.id as id, description, user_id, tender_id, " +
                "start_date as tender_start_date, end_date as tender_end_date, title as tender_title, category as tender_category, is_active as tender_is_active, price as tender_price, u.username, u.password as user_password, u.account_non_expired, u.account_non_locked, u.credentials_non_expired, u.enabled as user_enabled, u2.id as tender_user_id, u2.username as tender_username, u2.password as tender_user_password, u2.account_non_expired as tender_account_non_expired, u2.account_non_locked as tender_account_non_locked, u2.credentials_non_expired as tender_credentials_non_expired, u2.enabled as tender_user_enabled " +
                "FROM offer " +
                "INNER JOIN \"user\" u ON u.id=user_id " +
                "INNER JOIN tender t ON t.id=tender_id " +
                "INNER JOIN \"user\" u2 on u2.id = t.owner_id " +
                "WHERE id=:id";
        Map<String, Object> namedParams = Map.of("id", id);
        try {
            final Offer offer = jdbcTemplate.queryForObject(query, namedParams, resultSetToOffer);
            return Optional.ofNullable(offer);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
