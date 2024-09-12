package com.afpa.hebergement.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "floor")
public class Floor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('floor_id_floor_seq')")
    @Column(name = "id_floor", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "number_floor", nullable = false)
    private Integer numberFloor;

    @NotNull
    @Column(name = "is_for_women", nullable = false)
    private Boolean isForWomen = false;

    @OneToMany(mappedBy = "idFloor")
    private Set<Room> rooms = new LinkedHashSet<>();

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_afpa_center", nullable = false)
    private AfpaCenter idAfpaCenter;

}