package com.kpi.tendersystem.config;

import com.kpi.tendersystem.model.Offer;
import com.kpi.tendersystem.model.Tender;
import com.kpi.tendersystem.model.auth.User;
import com.kpi.tendersystem.model.form.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;
import java.util.Map;

@Configuration
@EnableTransactionManagement
public class RepositoryConfig {

    private static final String SELECT_AUTHORITIES = "SELECT user_id, authority_id, a.name as authority_name FROM user_authority INNER JOIN authority a on a.id=authority_id WHERE user_id=:user_id";

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Bean
    public RowMapper<Tender> resultSetToTender() {
        return (rs, row) -> {
            final Tender tender = new Tender();
            tender.setId(rs.getInt("tender_id"));
            tender.setTitle(rs.getString("tender_title"));
            tender.setStartDate(rs.getTimestamp("tender_start_date"));
            tender.setEndDate(rs.getTimestamp("tender_end_date"));
            tender.setCategory(Category.valueOf(rs.getString("tender_category")));
            tender.setActive(rs.getBoolean("tender_is_active"));
            tender.setPrice(rs.getDouble("tender_price"));
            Map<String, Object> userAuthoritiesParams = Map.of("user_id", rs.getInt("tender_user_id"));
            List<GrantedAuthority> authorities = jdbcTemplate.query(SELECT_AUTHORITIES,
                    userAuthoritiesParams, resultSetToGrantedAuthority());
            final User tenderOwner = new User(rs.getInt("tender_user_id"),
                    rs.getString("tender_username"),
                    rs.getString("tender_user_password"),
                    authorities,
                    rs.getBoolean("tender_account_non_expired"),
                    rs.getBoolean("tender_account_non_locked"),
                    rs.getBoolean("tender_credentials_non_expired"),
                    rs.getBoolean("tender_user_enabled"));
            tender.setOwner(tenderOwner);
            return tender;
        };
    }

    @Bean
    public RowMapper<GrantedAuthority> resultSetToGrantedAuthority() {
        return (resultSet, rowIndex) -> new SimpleGrantedAuthority(resultSet.getString("authority_name"));
    }

    @Bean
    public RowMapper<User> resultSetToUser() {
        return (rs, row) -> {
            Map<String, Object> userAuthoritiesParams = Map.of("user_id", rs.getInt("user_id"));
            List<GrantedAuthority> authorities = jdbcTemplate.query(SELECT_AUTHORITIES,
                    userAuthoritiesParams, resultSetToGrantedAuthority());
            final User user = new User(rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("user_password"),
                    authorities,
                    rs.getBoolean("account_non_expired"),
                    rs.getBoolean("account_non_locked"),
                    rs.getBoolean("credentials_non_expired"),
                    rs.getBoolean("user_enabled"));
            return user;
        };
    }

    @Bean
    public RowMapper<Offer> resultSetToOffer() {
        return (rs, row) -> {
            final Offer offer = new Offer();
            offer.setId(rs.getInt("id"));
            offer.setDescription(rs.getString("description"));
            offer.setUser(resultSetToUser().mapRow(rs, row));
            offer.setTender(resultSetToTender().mapRow(rs, row));
            return offer;
        };
    }
}
