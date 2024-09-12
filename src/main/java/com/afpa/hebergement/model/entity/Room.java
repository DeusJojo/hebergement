package com.afpa.hebergement.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('room_id_room_seq')")
    @Column(name = "id_room", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "room_number", nullable = false)
    private String roomNumber;

    @NotNull
    @Column(name = "room_key_number", nullable = false)
    private Integer roomKeyNumber;

    @NotNull
    @Column(name = "is_usable", nullable = false)
    private Boolean isUsable = false;

    @NotNull
    @Column(name = "badge_number", nullable = false)
    private Integer badgeNumber;

    @NotNull
    @Column(name = "fridge_key_number", nullable = false)
    private Integer fridgeKeyNumber;

    @NotNull
    @Column(name = "is_reserved", nullable = false)
    private Boolean isReserved = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_deposit")
    private Deposit idDeposit;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_floor", nullable = false)
    private Floor idFloor;

    @OneToMany(mappedBy = "idRoom")
    private Set<LeaseContract> leaseContracts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idRoom")
    private Set<Reservation> reservations = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idRoom")
    private Set<Work> works = new LinkedHashSet<>();

}