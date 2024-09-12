package com.afpa.hebergement.service.entity_service;

import com.afpa.hebergement.model.dto.ScheduleDTO;
import com.afpa.hebergement.service.GenericService;

import java.util.List;
import java.util.Optional;


public interface ScheduleService extends GenericService<ScheduleDTO, Integer> {

    //méthode pour update un schedule par id_schedule et id_intendant
    Optional<ScheduleDTO> updateByIdScheduleAndIdIntendant(Integer idSchedule, Integer idIntendant, ScheduleDTO scheduleDto);

    //méthode pour récupérer un schedule par id_schedule et id_intendant
    Optional<ScheduleDTO> getByIdScheduleAndIdIntendant(Integer idSchedule, Integer idIntendant);

    //méthode pour récupérer les schedules par l'id intendant
    List<ScheduleDTO> getByIdIntendant(Integer idIntendant);

    //méthode pour supprimer le(s) schedule(s) par son id_Intendant
    void deleteByIdIntendant(Integer idIntendant);

    //créer un schedule par id intendant et id day
    ScheduleDTO create (ScheduleDTO scheduleDto, Integer idIntendant,Integer idDay);
}
