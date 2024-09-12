package com.afpa.hebergement.model.mapper;


import com.afpa.hebergement.model.dto.WorkDTO;
import com.afpa.hebergement.model.entity.Work;

public class WorkMapper {

    // Constructeur privé pour empêcher l'instanciation de cette classe
    private WorkMapper() {

    }

    // Méthode pour mapper une entité Work vers un DTO WorkTypeDTO
    public static WorkDTO mapToWorkDTO(Work work) {

        // Crée un objet WorkTypeDTO
        WorkDTO workDTO = new WorkDTO();

        // Affecte les champs de l'entité Work au DTO WorkDTO
        workDTO.setId(work.getId());
        workDTO.setStartWork(work.getStartWork());
        workDTO.setEndWork(work.getEndWork());
        workDTO.setWorkCommentary(work.getWorkCommentary());

        //Mappage de Many to One
        workDTO.setIdWorkType(WorkTypeMapper.mapToWorkTypeDTO(work.getIdWorkType()));
        workDTO.setIdRoom(RoomMapper.mapToRoomDTO(work.getIdRoom()));

        // Retourne le DTO WorkDTO
        return workDTO;
    }


    // Méthode pour mapper un DTO WorkTypeDTO vers une entité WorkType
    public static Work mapToWorkEntity(WorkDTO workDto) {

        // Crée un objet Work
        Work work = new Work();

        // Affecte les champs de l'entité WorkDTO à l'entité Work
        work.setId(workDto.getId());
        work.setStartWork(workDto.getStartWork());
        work.setEndWork(workDto.getEndWork());

        //si work commentary n'est pas null alors faire le trim
        String workCommentary = workDto.getWorkCommentary();
        if (workCommentary != null) {
            workCommentary = workCommentary.trim();
        }
        work.setWorkCommentary(workCommentary);

        //Mappage de Many to One
        work.setIdWorkType(WorkTypeMapper.mapToWorkTypeEntity(workDto.getIdWorkType()));
        work.setIdRoom(RoomMapper.mapToRoomEntity(workDto.getIdRoom()));

        // Retourne l'entité Work
        return work;
    }
}
