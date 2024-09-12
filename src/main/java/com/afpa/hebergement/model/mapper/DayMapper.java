package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.DayDTO;
import com.afpa.hebergement.model.entity.Day;

public class DayMapper {

    // Constructeur privé pour empêcher l'instanciation de cette classe
    private DayMapper() {
    }


    // Méthode pour mapper une entité Day vers un DTO DayDTO
    public static DayDTO mapToDayDTO(Day day) {
        // Crée un objet DayDTO
        DayDTO dayDTO = new DayDTO();

        // Affecte les champs de l'entité Day au DTO DayDTO
        dayDTO.setId(day.getId());
        dayDTO.setWordingDay(day.getWordingDay().toLowerCase().trim());

        // Retourne le DTO DayDTODto
        return dayDTO;
    }


    // Méthode pour mapper un DTO DayDTO vers une entité Day
    public static Day mapToDayEntity(DayDTO dayDto) {
        // Crée un objet Day
        Day day = new Day();

        // Affecte les champs du DTO DayDTO à l'entité Day
        day.setId(dayDto.getId());
        day.setWordingDay(dayDto.getWordingDay().toLowerCase().trim());

        // Retourne l'entité Day
        return day;
    }
}

