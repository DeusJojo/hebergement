package com.afpa.hebergement.service.entity_service;

import com.afpa.hebergement.model.dto.FloorDTO;
import com.afpa.hebergement.service.GenericService;

import java.util.List;

public interface FloorService extends GenericService<FloorDTO, Integer> {

    //récupérer les étages par centre Afpa
    List<FloorDTO> getFloorsByAfpaCenter(Integer idAfpaCenter);

}
