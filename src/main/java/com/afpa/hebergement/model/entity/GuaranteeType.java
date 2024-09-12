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
@Table(name = "guarantee_type")
public class GuaranteeType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('guarantee_type_id_guarantee_type_seq')")
    @Column(name = "id_guarantee_type", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "wording_guarantee_type", nullable = false, length = 50)
    private String wordingGuaranteeType;

    @OneToMany(mappedBy = "idGuaranteeType")
    private Set<Guarantee> guarantees = new LinkedHashSet<>();

}