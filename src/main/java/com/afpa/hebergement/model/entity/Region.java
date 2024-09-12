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
@Table(name = "region")
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('region_id_region_seq')")
    @Column(name = "id_region", nullable = false)
    private Integer id;

    @Size(max = 150)
    @NotNull
    @Column(name = "region_name", nullable = false, length = 150)
    private String regionName;

    @OneToMany(mappedBy = "idRegion")
    private Set<Department> departments = new LinkedHashSet<>();

}