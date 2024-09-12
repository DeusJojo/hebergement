package com.afpa.hebergement.service.entity_service;

import com.afpa.hebergement.model.dto.RentDTO;
import com.afpa.hebergement.service.GenericService;

import java.util.List;

public interface RentService extends GenericService<RentDTO, Integer> {
    List<RentDTO> getAllByAfpaCenterId(Integer idAfpaCenter);
}
