package com.afpa.hebergement.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('inventory_id_inventory_seq')")
    @Column(name = "id_inventory", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "inventory_date", nullable = false)
    private LocalDate inventoryDate;

    @Size(max = 2000)
    @Column(name = "commentary", length = 2000)
    private String commentary;

    @Column(name = "deducted_guarantee", precision = 15, scale = 3)
    private BigDecimal deductedGuarantee;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_lease", nullable = false)
    private LeaseContract idLease;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_inventory_type", nullable = false)
    private InventoryType idInventoryType;

}