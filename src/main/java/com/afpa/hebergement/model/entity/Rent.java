package com.afpa.hebergement.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "rent")
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('rent_id_rent_seq')")
    @Column(name = "id_rent", nullable = false)
    private Integer id;

    @Size(max = 15)
    @NotNull
    @Column(name = "frequency", nullable = false, length = 15)
    private String frequency;

    @NotNull
    @Column(name = "amount", nullable = false, precision = 15, scale = 3)
    private BigDecimal amount;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_afpa_center", nullable = false)
    private AfpaCenter idAfpaCenter;

    @OneToMany(mappedBy = "idRent")
    private Set<LeaseContract> leaseContracts = new LinkedHashSet<>();

}