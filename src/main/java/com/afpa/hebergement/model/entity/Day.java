package com.afpa.hebergement.model.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "day")
public class Day {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_day", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "wordingDay", nullable = false, length = 8)
    private String wordingDay;

    @OneToMany(mappedBy = "idDay")
    private Set<Schedule> schedules = new LinkedHashSet<>();


}
