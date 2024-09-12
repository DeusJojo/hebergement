package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.ScheduleDTO;
import com.afpa.hebergement.model.entity.Schedule;



public class ScheduleMapper {

    // Constructeur privé pour empêcher l'instanciation de cette classe
    private ScheduleMapper() {

    }


    //Méthode pour mapper une entité Schedule vers un DTO ScheduleDTO.
    public static ScheduleDTO mapToScheduleDTO(Schedule schedule) {

        // Crée un objet ScheduleDTO
        ScheduleDTO scheduleDTO = new ScheduleDTO();

        // Affecte les champs de l'entité Schedule au DTO ScheduleDTO
        scheduleDTO.setId(schedule.getId());
        scheduleDTO.setStartMorning(schedule.getStartMorning()); //attribut pouvant être null
        scheduleDTO.setEndMorning(schedule.getEndMorning()); //attribut pouvant être null
        scheduleDTO.setStartAfternoon(schedule.getStartAfternoon()); //attribut pouvant être null
        scheduleDTO.setEndAfternoon(schedule.getEndAfternoon()); //attribut pouvant être null

        // Mappage de Many to One
        scheduleDTO.setIdIntendant(IntendantMapper.mapToIntendantDTO(schedule.getIdIntendant()));
        scheduleDTO.setIdDay(DayMapper.mapToDayDTO(schedule.getIdDay()));

        // Retourne le DTO ScheduleDTO
        return scheduleDTO;
    }


    //Méthode pour mapper un DTO ScheduleDTO vers une entité Schedule.
    public static Schedule mapToScheduleEntity(ScheduleDTO scheduleDto) {

        // Crée un objet Schedule
        Schedule schedule = new Schedule();

        // Affecte les champs du DTO ScheduleDTO à l'entité Schedule
        schedule.setId(scheduleDto.getId());
        schedule.setStartMorning(scheduleDto.getStartMorning()); //attribut pouvant être null
        schedule.setEndMorning(scheduleDto.getEndMorning()); //attribut pouvant être null
        schedule.setStartAfternoon(scheduleDto.getStartAfternoon()); //attribut pouvant être null
        schedule.setEndAfternoon(scheduleDto.getEndAfternoon()); //attribut pouvant être null

        // Mappage de Many to One
        schedule.setIdIntendant(IntendantMapper.mapToIntendantEntity(scheduleDto.getIdIntendant()));
        schedule.setIdDay(DayMapper.mapToDayEntity(scheduleDto.getIdDay()));

        // Retourne l'entité Schedule
        return schedule;
    }

}
