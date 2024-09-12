package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.DomaineDTO;
import com.afpa.hebergement.model.entity.Domaine;

import java.util.stream.Collectors;

public class DomaineMapper {
    private DomaineMapper() {}
    public static Domaine mapToDomaineEntity(DomaineDTO domaine){
        Domaine domaineEntity = new Domaine();
        domaineEntity.setId(domaine.getId());
        domaineEntity.setLibDomaine(domaine.getLibDomaine());
        domaineEntity.setGrn(domaine.getGrn());
        domaineEntity.setFormations(domaine.getFormations().stream().map(FormationMapper::mapToFormationEntity).collect(Collectors.toSet()));
        return domaineEntity;
    }

    public static DomaineDTO mapToDomaineDTO(Domaine domaine){
        DomaineDTO domaineDTO = new DomaineDTO();
        domaineDTO.setId(domaine.getId());
        domaineDTO.setLibDomaine(domaine.getLibDomaine());
        domaineDTO.setGrn(domaine.getGrn());
        domaineDTO.setFormations(domaine.getFormations().stream().map(FormationMapper::mapToFormationDTO).collect(Collectors.toSet()));
        return domaineDTO;
    }
}
