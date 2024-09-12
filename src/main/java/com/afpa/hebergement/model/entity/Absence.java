package com.afpa.hebergement.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "absence")
public class Absence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('absence_id_absence_seq')")
    @Column(name = "id_absence", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "start_abs_date", nullable = false)
    private LocalDate startAbsDate;

    @NotNull
    @Column(name = "end_abs_date", nullable = false)
    private LocalDate endAbsDate;

    @Size(max = 2000)
    @Column(name = "motive", length = 2000)
    private String motive;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_lease", nullable = false)
    private LeaseContract idLease;

}