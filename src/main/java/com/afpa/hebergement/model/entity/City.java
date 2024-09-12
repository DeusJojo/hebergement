package com.afpa.hebergement.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "city")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('city_id_city_seq')")
    @Column(name = "id_city", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "city_name", nullable = false)
    private String cityName;

    @Size(max = 5)
    @NotNull
    @Column(name = "postcode", nullable = false, length = 5)
    private String postcode;

    @Size(max = 5)
    @NotNull
    @Column(name = "insee_code", nullable = false, length = 5)
    private String inseeCode;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_department", nullable = false)
    private Department idDepartment;

    @OneToMany(mappedBy = "idCity")
    private Set<AfpaCenter> afpaCenters = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idCity")
    private Set<AppUser> appUsers = new LinkedHashSet<>();

}