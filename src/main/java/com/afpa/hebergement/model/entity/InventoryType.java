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
@Table(name = "inventory_type")
public class InventoryType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('inventory_type_id_inventory_type_seq')")
    @Column(name = "id_inventory_type", nullable = false)
    private Integer id;

    @Size(max = 10)
    @NotNull
    @Column(name = "wording_inventory_type", nullable = false, length = 10)
    private String wordingInventoryType;

    @OneToMany(mappedBy = "idInventoryType")
    private Set<Inventory> inventories = new LinkedHashSet<>();

}