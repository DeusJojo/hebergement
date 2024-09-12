package com.afpa.hebergement.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "offer")
public class Offer {
    @EmbeddedId
    private OfferId id;

    @MapsId("idService")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_service", nullable = false)
    private AppService idAppService;

    @MapsId("idAfpaCenter")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_afpa_center", nullable = false)
    private AfpaCenter idAfpaCenter;
}