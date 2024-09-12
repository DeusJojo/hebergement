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
@Table(name = "deposit_type")
public class DepositType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('deposit_type_id_deposit_type_seq')")
    @Column(name = "id_deposit_type", nullable = false)
    private Integer id;

    @Size(max = 10)
    @NotNull
    @Column(name = "wording_deposit_type", nullable = false, length = 10)
    private String wordingDepositType;

    @OneToMany(mappedBy = "idDepositType")
    private Set<Deposit> deposits = new LinkedHashSet<>();

}