package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.FormationDTO;
import com.afpa.hebergement.model.entity.Formation;

import java.util.stream.Collectors;

public class FormationMapper {

    private FormationMapper() {}

    public static Formation mapToFormationEntity(FormationDTO entity) {
        Formation formation = new Formation();
        formation.setId(entity.getId());
        formation.setFormationName(entity.getFormationName());
        formation.setIdDomaine(DomaineMapper.mapToDomaineEntity(entity.getDomain()));
        formation.setSessionFormations(entity.getSessionsFormations().stream().map(SessionFormationMapper::mapToSessionFormationEntity).collect(Collectors.toSet()));
        return formation;
    }

    public static FormationDTO mapToFormationDTO(Formation entity) {
        FormationDTO dto = new FormationDTO();
        dto.setId(entity.getId());
        dto.setFormationName(entity.getFormationName());
        dto.setDomain(DomaineMapper.mapToDomaineDTO(entity.getIdDomaine()));
        dto.setSessionsFormations(entity.getSessionFormations().stream().map(SessionFormationMapper::mapToSessionFormationDTO).collect(Collectors.toSet()));
        return dto;
    }
}
