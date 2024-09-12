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
@Table(name = "formation")
public class Formation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('formation_id_formation_seq')")
    @Column(name = "id_formation", nullable = false)
    private Integer id;

    @Size(max = 200)
    @NotNull
    @Column(name = "formation_name", nullable = false, length = 200)
    private String formationName;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_domaine", nullable = false)
    private Domaine idDomaine;

    @OneToMany(mappedBy = "idFormation")
    private Set<SessionFormation> sessionFormations = new LinkedHashSet<>();

}