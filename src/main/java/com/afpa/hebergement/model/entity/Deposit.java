package com.afpa.hebergement.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "deposit")
public class Deposit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('deposit_id_deposit_seq')")
    @Column(name = "id_deposit", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "deposit_date", nullable = false)
    private LocalDate depositDate;

    @Column(name = "back_depo_date")
    private LocalDate backDepoDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_guarantee", nullable = false)
    private Guarantee idGuarantee;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_user", nullable = false)
    private AppUser idUser;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_deposit_type", nullable = false)
    private DepositType idDepositType;

    @OneToMany(mappedBy = "idDeposit")
    private Set<Room> rooms = new LinkedHashSet<>();

}