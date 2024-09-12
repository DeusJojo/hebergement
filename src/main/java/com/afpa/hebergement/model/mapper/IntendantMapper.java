package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.IntendantDTO;
import com.afpa.hebergement.model.entity.Intendant;


public class IntendantMapper {

    // Constructeur privé pour empêcher l'instanciation de cette classe
    private IntendantMapper() {

    }


    //Méthode pour mapper une entité Intendant vers un DTO IntendantDTO.
    public static IntendantDTO mapToIntendantDTO(Intendant intendant) {

        // Crée un objet IntendantDTO
        IntendantDTO intendantDTO = new IntendantDTO();

        // Affecte les champs de l'entité Intendant au DTO IntendantDTO
        intendantDTO.setId(intendant.getId());
        intendantDTO.setStartDateIntendant(intendant.getStartDateIntendant()); //attribut pouvant être null
        intendantDTO.setEndDateIntendant(intendant.getEndDateIntendant()); //attribut pouvant être null

        // Mappage de Many to One
        intendantDTO.setIdUser(AppUserMapper.mapToAppUserDTO(intendant.getIdUser()));


        // Retourne le DTO IntendantDTO
        return intendantDTO;
    }


    //Méthode pour mapper un DTO IntendantDTO vers une entité Intendant.
    public static Intendant mapToIntendantEntity(IntendantDTO intendantDto) {

        // Crée un objet Intendant
        Intendant intendant = new Intendant();

        // Affecte les champs du DTO IntendantDTO à l'entité Intendant
        intendant.setId(intendantDto.getId());
        intendant.setStartDateIntendant(intendantDto.getStartDateIntendant()); //attribut pouvant être null
        intendant.setEndDateIntendant(intendantDto.getEndDateIntendant()); //attribut pouvant être null

        // Mappage de Many to One
        intendant.setIdUser(AppUserMapper.mapToAppUser(intendantDto.getIdUser()));

        // Retourne l'entité Intendant
        return intendant;
    }

}
