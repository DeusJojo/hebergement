package com.afpa.hebergement.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "financier")
public class Financier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('financier_id_financier_seq')")
    @Column(name = "id_financier", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "covering_cost", nullable = false, precision = 15, scale = 3)
    private BigDecimal coveringCost;

    @Size(max = 255)
    @NotNull
    @Column(name = "log_name", nullable = false)
    private String logName;

    @Size(max = 255)
    @Column(name = "url_logo_financier")
    private String urlLogoFinancier;

    @OneToMany(mappedBy = "idFinancier")
    private Set<Participate> participates = new LinkedHashSet<>();

}