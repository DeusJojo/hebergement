package com.afpa.hebergement.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "participate")
public class Participate {
    @EmbeddedId
    private ParticipateId id;

    @MapsId("idSession")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_session", nullable = false)
    private SessionFormation idSession;

    @MapsId("idUser")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_user", nullable = false)
    private AppUser idUser;

    @MapsId("idFinancier")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_financier", nullable = false)
    private Financier idFinancier;

}