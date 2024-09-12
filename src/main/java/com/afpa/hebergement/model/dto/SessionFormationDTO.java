package com.afpa.hebergement.model.dto;

import com.afpa.hebergement.model.entity.Participate;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class SessionFormationDTO {
    private Integer id;
    private LocalDate startDateSession;
    private LocalDate endDateSession;
    private String offerNumber;
    private FormationDTO formation;
    private Set<AppUserDTO> Users;
}
