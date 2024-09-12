package com.afpa.hebergement.service.entity_service;

import com.afpa.hebergement.model.dto.CityDTO;
import com.afpa.hebergement.service.GenericService;

import java.util.Optional;

public interface CityService extends GenericService<CityDTO, Integer> {
    Optional<CityDTO> getByInseeCodeAndPostCode(String inseeCode, String postCode);
}
