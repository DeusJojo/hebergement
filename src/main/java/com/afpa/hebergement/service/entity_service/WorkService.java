package com.afpa.hebergement.service.entity_service;


import com.afpa.hebergement.model.dto.WorkDTO;
import com.afpa.hebergement.service.GenericService;

import java.util.List;

public interface WorkService extends GenericService<WorkDTO, Integer> {

    //récupérer les chambres en travaux pour un centre afpa
    List<WorkDTO> getWorksByAfpaCenter(Integer idAfpaCenter);

}
