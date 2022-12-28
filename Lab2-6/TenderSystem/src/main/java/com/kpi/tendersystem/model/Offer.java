package com.kpi.tendersystem.model;

import com.kpi.tendersystem.model.form.FormOffer;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NamedQueries({
        @NamedQuery(name = Offer.SELECT_ALL_BY_USERNAME, query = "select o from Offer o inner join fetch o.user u where u.username=:username"),
        @NamedQuery(name = Offer.SELECT_BY_USERNAME_AND_TENDER_ID, query = "select o from Offer o inner join fetch o.user u inner join fetch o.tender t where u.username=:username and t.id=:tenderId"),
        @NamedQuery(name = Offer.SELECT_BY_TENDER_ID, query = "select o from Offer o inner join fetch o.tender t where t.id=:id"),
        @NamedQuery(name = Offer.SELECT_BY_ID, query = "select o from Offer o where o.id=:id")
})
public class Offer extends FormOffer implements Serializable {

    public static final String SELECT_ALL_BY_USERNAME = "Offer.SELECT_ALL_BY_USERNAME";

    public static final String SELECT_BY_USERNAME_AND_TENDER_ID = "Offer.SELECT_BY_USERNAME_AND_TENDER_ID";

    public static final String SELECT_BY_TENDER_ID = "Offer.SELECT_BY_TENDER_ID";

    public static final String SELECT_BY_ID = "Offer.SELECT_BY_ID";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tender_id", nullable = false)
    private Tender tender;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Tender getTender() {
        return tender;
    }

    public void setTender(Tender tender) {
        this.tender = tender;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
