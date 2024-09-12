package com.afpa.hebergement.model.dto;

import lombok.Data;

import java.util.Set;

@Data
public class FormationDTO {
    private Integer id;
    private  String formationName;
    private DomaineDTO domain;
    private Set<SessionFormationDTO> sessionsFormations;
}
