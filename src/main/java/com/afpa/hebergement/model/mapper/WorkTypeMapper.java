package com.afpa.hebergement.model.mapper;


import com.afpa.hebergement.model.dto.WorkTypeDTO;
import com.afpa.hebergement.model.entity.WorkType;

public class WorkTypeMapper {

    // Constructeur privé pour empêcher l'instanciation de cette classe
    private WorkTypeMapper() {
    }


    // Méthode pour mapper une entité WorkType vers un DTO WorkTypeDTO
    public static WorkTypeDTO mapToWorkTypeDTO(WorkType workType) {

        // Crée un objet WorkTypeDTO
        WorkTypeDTO workTypeDTO = new WorkTypeDTO();

        // Affecte les champs de l'entité WorkType au DTO WorkTypeDTO
        workTypeDTO.setId(workType.getId());
        workTypeDTO.setWordingWorkType(workType.getWordingWorkType());

        // Retourne le DTO WorkTypeDTO
        return workTypeDTO;
    }


    // Méthode pour mapper un DTO WorkTypeDTO vers une entité WorkType
    public static WorkType mapToWorkTypeEntity(WorkTypeDTO workTypeDto) {

        // Crée un objet WorkType
        WorkType workType = new WorkType();

        // Affecte les champs de l'entité WorkTypeDTO à l'entité WorkType
        workType.setId(workTypeDto.getId());
        workType.setWordingWorkType(workTypeDto.getWordingWorkType().toLowerCase().trim());

        // Retourne l'entité WorkType
        return workType;
    }

}
