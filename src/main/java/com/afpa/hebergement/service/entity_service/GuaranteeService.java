package com.afpa.hebergement.service.entity_service;

import com.afpa.hebergement.model.dto.GuaranteeDTO;
import com.afpa.hebergement.service.GenericService;

import java.util.List;
import java.util.Optional;

public interface GuaranteeService extends GenericService<GuaranteeDTO, Integer> {
    Optional<GuaranteeDTO> getByIdGuaranteeTypeAndAfpaCenter(Integer idGuaranteeType, Integer idAfpaCenter);
    List<GuaranteeDTO> getAllByAfpaCenterId(Integer idAfpaCenter);
}
