package com.afpa.hebergement.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('appointment_id_appointment_seq')")
    @Column(name = "id_appointment", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "date_appointment", nullable = false)
    private LocalDateTime dateAppointment;

    @Size(max = 100)
    @NotNull
    @Column(name = "reason", nullable = false, length = 100)
    private String reason;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_intendant", nullable = false)
    private Intendant idIntendant;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_user", nullable = false)
    private AppUser idUser;

}