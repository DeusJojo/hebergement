package com.afpa.hebergement.model.dto;

import lombok.Data;

import java.util.Set;

@Data
public class DomaineDTO {
    private Integer id;
    private String libDomaine;
    private String grn;
    private Set<FormationDTO> formations;
}
