package com.afpa.hebergement.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class AppServiceDTO {
    private Integer id;
    private String wordingService;
    private Set<AfpaCenterDTO> afpaCenters;
}
