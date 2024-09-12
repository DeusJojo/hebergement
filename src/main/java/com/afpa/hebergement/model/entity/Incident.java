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
@Table(name = "incident")
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('incident_id_incident_seq')")
    @Column(name = "id_incident", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "incident_date", nullable = false)
    private LocalDate incidentDate;

    @Size(max = 2000)
    @Column(name = "commentary_user", length = 2000)
    private String commentaryUser;

    @Size(max = 2000)
    @Column(name = "commentary_admin", length = 2000)
    private String commentaryAdmin;

    @Size(max = 100)
    @Column(name = "img_incident", length = 100)
    private String imgIncident;

    @NotNull
    @Column(name = "is_closed", nullable = false)
    private Boolean isClosed = false;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_lease", nullable = false)
    private LeaseContract idLease;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_incident_type", nullable = false)
    private IncidentType idIncidentType;

    @OneToMany(mappedBy = "idIncident")
    private Set<Warning> warnings = new LinkedHashSet<>();

}