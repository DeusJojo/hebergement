package com.afpa.hebergement.model.dto;

import com.afpa.hebergement.model.entity.Participate;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class FinancierDTO {
    private Integer id;
    private BigDecimal coveringCost;
    private String logName;
    private String urlLogoFinancier;
    private Set<Participate> participates;
}
