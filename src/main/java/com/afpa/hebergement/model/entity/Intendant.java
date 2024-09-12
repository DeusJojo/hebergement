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
@Table(name = "intendant")
public class Intendant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('intendant_id_intendant_seq')")
    @Column(name = "id_intendant", nullable = false)
    private Integer id;

    @Column(name = "start_date_intendant")
    private LocalDate startDateIntendant;

    @Column(name = "end_date_intendant")
    private LocalDate endDateIntendant;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_user", nullable = false)
    private AppUser idUser;

    @OneToMany(mappedBy = "idIntendant")
    private Set<Appointment> appointments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idIntendant")
    private Set<Message> messages = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idIntendant")
    private Set<Schedule> schedules = new LinkedHashSet<>();

}