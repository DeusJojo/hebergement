package com.afpa.hebergement.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "bill")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('bill_id_bill_seq')")
    @Column(name = "id_bill", nullable = false)
    private Integer id;

    @Size(max = 10)
    @NotNull
    @Column(name = "bill_number", nullable = false, length = 10)
    private String billNumber;

    @NotNull
    @Column(name = "total_amount", nullable = false, precision = 15, scale = 3)
    private BigDecimal totalAmount;

    @NotNull
    @Column(name = "bill_date", nullable = false)
    private LocalDate billDate;

    @NotNull
    @Column(name = "is_payed", nullable = false)
    private Boolean isPayed = false;

    @Column(name = "payment_date_bill")
    private LocalDate paymentDateBill;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_lease", nullable = false)
    private LeaseContract idLease;

    @ManyToMany
    @JoinTable(name = "use",
            joinColumns = @JoinColumn(name = "id_bill"),
            inverseJoinColumns = @JoinColumn(name = "id_payment_type"))
    private Set<PaymentType> paymentTypes = new LinkedHashSet<>();

}