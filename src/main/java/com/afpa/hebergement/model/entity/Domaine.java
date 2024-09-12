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
@Table(name = "domaine")
public class Domaine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('domaine_id_domaine_seq')")
    @Column(name = "id_domaine", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "lib_domaine", nullable = false)
    private String libDomaine;

    @Size(max = 3)
    @Column(name = "grn", length = 3)
    private String grn;

    @OneToMany(mappedBy = "idDomaine")
    private Set<Formation> formations = new LinkedHashSet<>();

}