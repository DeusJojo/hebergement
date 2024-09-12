package com.afpa.hebergement.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "incident_type")
public class IncidentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('incident_type_id_incident_type_seq')")
    @Column(name = "id_incident_type", nullable = false)
    private Integer id;

    @Size(max = 30)
    @NotNull
    @Column(name = "wording_incident_type", nullable = false, length = 30)
    private String wordingIncidentType;

    @OneToMany(mappedBy = "idIncidentType")
    private Set<Incident> incidents = new LinkedHashSet<>();

}