package com.afpa.hebergement.model.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RentDTO {

    private Integer id;
    private String frequency;
    private BigDecimal amount;
    private AfpaCenterDTO idAfpaCenter;
}
