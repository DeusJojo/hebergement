package com.afpa.hebergement.service.entity_service;

import com.afpa.hebergement.model.dto.DepartmentDTO;
import com.afpa.hebergement.service.GenericService;

import java.util.Optional;

public interface DepartmentService extends GenericService<DepartmentDTO, Integer> {
    Optional<DepartmentDTO> getByDepartmentCode(String departmentCode);
}
