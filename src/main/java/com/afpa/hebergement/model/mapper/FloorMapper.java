package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.FloorDTO;
import com.afpa.hebergement.model.entity.Floor;


public class FloorMapper {

    // Constructeur privé pour empêcher l'instanciation de cette classe
    private FloorMapper() {
    }


    // Méthode pour mapper une entité Floor vers un DTO FloorDTO
    public static FloorDTO mapToFloorDTO(Floor floor) {

        //creation d'une entité floor DTO
        FloorDTO floorDTO = new FloorDTO();

        //mappage des attributs
        floorDTO.setId(floor.getId()); //id de l'étage
        floorDTO.setNumberFloor(floor.getNumberFloor()); // numéro étage
        floorDTO.setIsForWomen(floor.getIsForWomen()); // si étage réservé aux femmes

        //mappage Many to One
        floorDTO.setIdAfpaCenter(AfpaCenterMapper.mapToAfpaCenterDto(floor.getIdAfpaCenter()));

        //retourne le floorDTO
        return floorDTO;
    }


    // Méthode pour mapper un DTO FloorDTO vers une entité Floor
    public static Floor mapToFloorEntity(FloorDTO floorDto) {


        //creation d'une entité floor DTO
        Floor floor = new Floor();

        //mappage des attributs
        floor.setId(floorDto.getId()); //id de l'étage
        floor.setNumberFloor(floorDto.getNumberFloor()); // numéro étage
        floor.setIsForWomen(floorDto.getIsForWomen()); // si étage réservé aux femmes

        //mappage Many To one
        floor.setIdAfpaCenter(AfpaCenterMapper.mapToAfpaCenter(floorDto.getIdAfpaCenter()));

        //retourne le Floor entity
        return floor;
    }


}

