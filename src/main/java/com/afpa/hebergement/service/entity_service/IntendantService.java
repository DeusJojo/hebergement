package com.afpa.hebergement.service.entity_service;

import com.afpa.hebergement.model.dto.IntendantDTO;
import com.afpa.hebergement.service.GenericService;

import java.util.List;

public interface IntendantService extends GenericService<IntendantDTO, Integer> {

    //récupérer tous les intendants d'un centre afpa
    List<IntendantDTO> getAllIntendantsByAfpaCenter(Integer afpaCenterId);

}
