package com.afpa.hebergement.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "session_formation")
public class SessionFormation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('session_formation_id_session_seq')")
    @Column(name = "id_session", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "start_date_session", nullable = false)
    private LocalDate startDateSession;

    @NotNull
    @Column(name = "end_date_session", nullable = false)
    private LocalDate endDateSession;

    @Size(max = 20)
    @NotNull
    @Column(name = "offer_number", nullable = false, length = 20)
    private String offerNumber;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_formation", nullable = false)
    private Formation idFormation;

    @OneToMany(mappedBy = "idSession")
    private Set<Participate> participates = new LinkedHashSet<>();

}