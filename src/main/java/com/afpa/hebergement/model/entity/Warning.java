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
@Table(name = "warning")
public class Warning {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('warning_id_warning_seq')")
    @Column(name = "id_warning", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "warning_date", nullable = false)
    private LocalDateTime warningDate;

    @Size(max = 2000)
    @Column(name = "commentary", length = 2000)
    private String commentary;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_incident", nullable = false)
    private Incident idIncident;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_user", nullable = false)
    private AppUser idUser;

}