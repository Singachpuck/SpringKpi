package com.kpi.tendersystem.dao.impl.jdbc;

import com.kpi.tendersystem.dao.TenderDao;
import com.kpi.tendersystem.model.Tender;
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
public class JdbcTenderDao implements TenderDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private RowMapper<Tender> resultSetToTender;

    @Override
    @Transactional(readOnly = true)
    public Collection<Tender> getAll(int offset, int limit) {
        final String query = "SELECT t.id as tender_id, " +
                "       start_date as tender_start_date, end_date as tender_end_date, title as tender_title, category as tender_category, is_active as tender_is_active, price as tender_price, u2.id as tender_user_id, u2.username as tender_username, u2.password as tender_user_password, u2.account_non_expired as tender_account_non_expired, u2.account_non_locked as tender_account_non_locked, u2.credentials_non_expired as tender_credentials_non_expired, u2.enabled as tender_user_enabled " +
                "FROM tender t " +
                "INNER JOIN \"user\" u2 on u2.id = t.owner_id " +
                "LIMIT :limit OFFSET :offset";
        Map<String, Object> namedParams = Map.of("limit", limit, "offset", offset);
        return jdbcTemplate.query(query, namedParams, resultSetToTender);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Tender> getAll() {
        final String query = "SELECT t.id as tender_id, " +
                "       start_date as tender_start_date, end_date as tender_end_date, title as tender_title, category as tender_category, is_active as tender_is_active, price as tender_price, u2.id as tender_user_id, u2.username as tender_username, u2.password as tender_user_password, u2.account_non_expired as tender_account_non_expired, u2.account_non_locked as tender_account_non_locked, u2.credentials_non_expired as tender_credentials_non_expired, u2.enabled as tender_user_enabled " +
                "FROM tender t " +
                "INNER JOIN \"user\" u2 on u2.id = t.owner_id";
        return jdbcTemplate.query(query, resultSetToTender);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tender> getById(int id) {
        final String query = "SELECT t.id as tender_id, " +
                "       start_date as tender_start_date, end_date as tender_end_date, title as tender_title, category as tender_category, is_active as tender_is_active, price as tender_price, u2.id as tender_user_id, u2.username as tender_username, u2.password as tender_user_password, u2.account_non_expired as tender_account_non_expired, u2.account_non_locked as tender_account_non_locked, u2.credentials_non_expired as tender_credentials_non_expired, u2.enabled as tender_user_enabled " +
                "FROM tender t " +
                "INNER JOIN \"user\" u2 on u2.id = t.owner_id " +
                "WHERE t.id=:id";
        Map<String, Object> namedParams = Map.of("id", id);
        try {
            final Tender tender = jdbcTemplate.queryForObject(query, namedParams, resultSetToTender);
            return Optional.ofNullable(tender);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public int add(Tender tender) {
        final String query = "INSERT INTO tender(owner_id, start_date, end_date, title, category, price) " +
                "VALUES (:owner_id, :start_date, :end_date, :title, :category, :price)";
        Map<String, Object> namedParams = Map.of(
                "owner_id", tender.getOwner().getId(),
                "start_date", tender.getStartDate(),
                "end_date", tender.getEndDate(),
                "title", tender.getTitle(),
                "category", tender.getCategory().name(),
                "price", tender.getPrice()
        );
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(query, new MapSqlParameterSource(namedParams), keyHolder, new String[] { "id" });
        return (int) keyHolder.getKey();
    }

    @Override
    public void update(Tender tender) {
        final String query = "UPDATE tender " +
                "SET owner_id=:owner_id, start_date=:start_date, end_date=:end_date, title=:title, \"category\"=:category, price=:price, is_active=:is_active " +
                "WHERE tender.id=:id";
        Map<String, Object> namedParams = Map.of(
                "id", tender.getId(),
                "owner_id", tender.getOwner().getId(),
                "start_date", tender.getStartDate(),
                "end_date", tender.getEndDate(),
                "title", tender.getTitle(),
                "category", tender.getCategory().name(),
                "price", tender.getPrice(),
                "is_active", tender.isActive()
        );
        jdbcTemplate.update(query, namedParams);
    }

    @Override
    public void delete(int id) {
        final String query = "DELETE FROM tender WHERE tender.id=:id";
        Map<String, Object> namedParams = Map.of("id", id);
        jdbcTemplate.update(query, namedParams);
    }
}
