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
@Table(name = "service")
public class AppService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('service_id_service_seq')")
    @Column(name = "id_service", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "wording_service", nullable = false)
    private String wordingService;

    @ManyToMany
    @JoinTable(name = "offer",
            joinColumns = @JoinColumn(name = "id_service"),
            inverseJoinColumns = @JoinColumn(name = "id_afpa_center"))
    private Set<AfpaCenter> afpaCenters = new LinkedHashSet<>();
}