package com.afpa.hebergement.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;


import java.sql.Time;


@Getter
@Setter
@Entity
@Table(name = "schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('schedule_id_schedule_seq'")
    @Column(name = "id_schedule", nullable = false)
    private Integer id;

    @Column(name = "start_morning")
    private Time startMorning;

    @Column(name = "end_morning")
    private Time endMorning;

    @Column(name = "start_afternoon")
    private Time startAfternoon;

    @Column(name = "end_afternoon")
    private Time endAfternoon;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_intendant", nullable = false)
    private Intendant idIntendant;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_day", nullable = false)
    private Day idDay;

}