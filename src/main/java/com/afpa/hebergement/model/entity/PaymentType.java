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
@Table(name = "payment_type")
public class PaymentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('payment_type_id_payment_type_seq')")
    @Column(name = "id_payment_type", nullable = false)
    private Integer id;

    @Size(max = 20)
    @NotNull
    @Column(name = "wording_payment_type", nullable = false, length = 20)
    private String wordingPaymentType;

    @ManyToMany(mappedBy = "paymentTypes")
    private Set<Bill> bills = new LinkedHashSet<>();

}