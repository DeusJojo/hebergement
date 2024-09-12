package com.afpa.hebergement.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "work_demand")
public class WorkDemand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('work_demand_id_work_demand_seq')")
    @Column(name = "id_work_demand", nullable = false)
    private Integer id;

    @Size(max = 2000)
    @NotNull
    @Column(name = "demand_description", nullable = false, length = 2000)
    private String demandDescription;

    @Size(max = 100)
    @Column(name = "img_demand", length = 100)
    private String imgDemand;

    @Size(max = 8)
    @Column(name = "img_code", length = 8)
    private String imgCode;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_lease", nullable = false)
    private LeaseContract idLease;

}