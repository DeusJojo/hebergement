package com.afpa.hebergement.model.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class GuaranteeDTO {

    private Integer id;
    private BigDecimal amount;
    private AfpaCenterDTO idAfpaCenter;
    private GuaranteeTypeDTO idGuaranteeType;
}
