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
@Table(name = "civility")
public class Civility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('civility_id_civility_seq')")
    @Column(name = "id_civility", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "wording_civility", nullable = false, length = 50)
    private String wordingCivility;

    @OneToMany(mappedBy = "idCivility")
    private Set<AppUser> appUsers = new LinkedHashSet<>();

}