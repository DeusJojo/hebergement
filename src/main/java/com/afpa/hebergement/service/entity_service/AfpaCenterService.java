package com.afpa.hebergement.service.entity_service;

import com.afpa.hebergement.model.dto.AfpaCenterDTO;
import com.afpa.hebergement.service.GenericService;

import java.util.Optional;

public interface AfpaCenterService extends GenericService<AfpaCenterDTO, Integer> {
    Optional<AfpaCenterDTO> getByCenterName(String centerName);
    Optional<AfpaCenterDTO> getByCityName(String city);
}
