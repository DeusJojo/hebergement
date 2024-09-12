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
@Table(name = "work_type")
public class WorkType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('work_type_id_work_type_seq')")
    @Column(name = "id_work_type", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "wording_work_type", nullable = false, length = 100)
    private String wordingWorkType;

    @OneToMany(mappedBy = "idWorkType")
    private Set<Work> works = new LinkedHashSet<>();

}