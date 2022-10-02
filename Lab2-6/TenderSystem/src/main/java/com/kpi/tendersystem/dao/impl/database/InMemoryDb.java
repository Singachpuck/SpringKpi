package com.kpi.tendersystem.dao.impl.database;

import com.kpi.tendersystem.model.Offer;
import com.kpi.tendersystem.model.Tender;
import com.kpi.tendersystem.model.auth.Authorities;
import com.kpi.tendersystem.model.auth.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

public class InMemoryDb {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    private static final Collection<Tender> tenders = new ArrayList<>(10);

    private static final Collection<User> users = new ArrayList<>(10);

    private static final Collection<Offer> offers = new ArrayList<>(10);

    static {
        // Users
        final User adam = new User(
                1,
                "adam",
                passwordEncoder.encode("adam"),
                List.of(Authorities.DEFAULT.getGrantedAuthority()),
                true,
                true,
                true,
                true);

        final User linda = new User(
                2,
                "linda",
                passwordEncoder.encode("linda"),
                List.of(Authorities.DEFAULT.getGrantedAuthority()),
                true,
                true,
                true,
                true);

        users.add(adam);
        users.add(linda);

        // Tenders
        final Tender tender1 = new Tender();

        tender1.setId(1);
        tender1.setTitle("Tender 1");
        tender1.setPrice(1_000_000D);
        tender1.setStartDate(new Date(122, Calendar.NOVEMBER, 1));
        tender1.setEndDate(new Date(122, Calendar.DECEMBER, 1));
        tender1.setActive(true);
        tender1.setOwner(linda);

        tenders.add(tender1);
    }

    public static Collection<Tender> loadTenders() {
        return tenders;
    }

    public static Collection<User> loadUsers() {
        return users;
    }

    public static Collection<Offer> loadOffers() {
        return offers;
    }
}
