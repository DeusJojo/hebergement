package com.afpa.hebergement.service.entity_service;

import com.afpa.hebergement.model.dto.RegionDTO;
import com.afpa.hebergement.service.GenericService;

import java.util.Optional;

public interface RegionService extends GenericService<RegionDTO, Integer> {
    Optional<RegionDTO> getByRegionName(String regionName);
}
