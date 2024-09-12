package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.CivilityDTO;
import com.afpa.hebergement.model.entity.Civility;


public class CivilityMapper {


    // Constructeur privé pour empêcher l'instanciation de cette classe
    private CivilityMapper() {
    }

    // Méthode pour mapper une entité Civility vers un DTO CivilityDTO
    public static CivilityDTO mapToCivilityDTO(Civility civility) {

        // Créer un objet CivilityDTO
        CivilityDTO civilityDTO = new CivilityDTO();

        // Affecte les champs de l'entité Civility au DTO CivilityDTO
        civilityDTO.setId(civility.getId());
        civilityDTO.setWordingCivility(civility.getWordingCivility());

        // Retourne le DTO CivilityDTO
        return civilityDTO;
    }


    // Méthode pour mapper un DTO CivilityDTO vers une entité Civility
    public static Civility mapToCivilityEntity(CivilityDTO civilityDto) {
        // Créer un objet Civility
        Civility civility = new Civility();

        // Affecte les champs du DTO CivilityDTO à l'entité Civility
        civility.setId(civilityDto.getId());
        civility.setWordingCivility(civilityDto.getWordingCivility());

        // Retourne l'entité Civility
        return civility;
    }

}
