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
@Table(name = "lease_contract")
public class LeaseContract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('lease_contract_id_lease_seq')")
    @Column(name = "id_lease", nullable = false)
    private Integer id;

    @Column(name = "start_date_lease")
    private LocalDate startDateLease;

    @Column(name = "end_date_lease")
    private LocalDate endDateLease;

    @Column(name = "is_present")
    private Boolean isPresent = false;

    @Column(name = "is_signed")
    private Boolean isSigned = false;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_user", nullable = false)
    private AppUser idUser;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_room", nullable = false)
    private Room idRoom;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_rent", nullable = false)
    private Rent idRent;

    @OneToMany(mappedBy = "idLease")
    private Set<Absence> absences = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idLease")
    private Set<Bill> bills = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idLease")
    private Set<Incident> incidents = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idLease")
    private Set<Inventory> inventories = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idLease")
    private Set<WorkDemand> workDemands = new LinkedHashSet<>();

}