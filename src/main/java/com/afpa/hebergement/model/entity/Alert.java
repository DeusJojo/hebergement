package com.afpa.hebergement.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "alert")
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('alert_id_alert_seq')")
    @Column(name = "id_alert", nullable = false)
    private Integer id;

    @Size(max = 15)
    @NotNull
    @Column(name = "wording_alert", nullable = false, length = 15)
    private String wordingAlert;

    @Size(max = 2000)
    @NotNull
    @Column(name = "commentary_alert", nullable = false, length = 2000)
    private String commentaryAlert;

    @NotNull
    @Column(name = "date_alert", nullable = false)
    private LocalDate dateAlert;

    @NotNull
    @Column(name = "is_archived", nullable = false)
    private Boolean isArchived = false;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_afpa_center", nullable = false)
    private AfpaCenter idAfpaCenter;

    @ManyToMany(mappedBy = "alerts")
    private Set<AppUser> appUsers = new LinkedHashSet<>();

}