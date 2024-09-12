package com.afpa.hebergement.model.dto;

import lombok.Data;

import java.util.Set;

@Data
public class DepositTypeDTO {

    private int id;
    private String wordingDepositType;
    private Set<DepositDTO> depositsDTO;
}
