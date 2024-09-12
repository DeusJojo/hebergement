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
@Table(name = "reservation_motive")
public class ReservationMotive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('reservation_motive_id_reservation_motive_seq')")
    @Column(name = "id_reservation_motive", nullable = false)
    private Integer id;

    @Size(max = 20)
    @NotNull
    @Column(name = "motive", nullable = false, length = 20)
    private String motive;

    @OneToMany(mappedBy = "idReservationMotive")
    private Set<Reservation> reservations = new LinkedHashSet<>();

}