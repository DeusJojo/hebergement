package com.afpa.hebergement.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "see")
public class See {
    @EmbeddedId
    private SeeId id;

    @MapsId("idUser")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_user", nullable = false)
    private AppUser idUser;

    @MapsId("idAlert")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_alert", nullable = false)
    private Alert idAlert;

}