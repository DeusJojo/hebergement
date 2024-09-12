package com.afpa.hebergement.service.entity_service;

import com.afpa.hebergement.model.dto.AppServiceDTO;
import com.afpa.hebergement.service.GenericService;

import java.util.List;

public interface AppServiceService extends GenericService<AppServiceDTO, Integer> {

    List<AppServiceDTO> getAllByAfpaCenterId(Integer idAfpaCenter);
}
