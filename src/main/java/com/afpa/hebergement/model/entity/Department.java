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
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('department_id_department_seq')")
    @Column(name = "id_department", nullable = false)
    private Integer id;

    @Size(max = 150)
    @NotNull
    @Column(name = "department_name", nullable = false, length = 150)
    private String departmentName;

    @Size(max = 3)
    @NotNull
    @Column(name = "department_code", nullable = false, length = 3)
    private String departmentCode;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_region", nullable = false)
    private Region idRegion;

    @OneToMany(mappedBy = "idDepartment")
    private Set<City> cities = new LinkedHashSet<>();

}