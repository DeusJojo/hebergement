package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.RoomDTO;
import com.afpa.hebergement.model.entity.Room;


public class RoomMapper {

    // Constructeur privé pour empêcher l'instanciation de cette classe
    private RoomMapper() {
    }


    // Méthode pour mapper une entité Room vers un DTO RoomDTO
    public static RoomDTO mapToRoomDTO(Room room) {

        // créer un objet RoomDTO
        RoomDTO roomDTO = new RoomDTO();

        // Affecte les champs de l'entité Room au DTO RoomDTO
        roomDTO.setId(room.getId());
        roomDTO.setRoomNumber(room.getRoomNumber());
        roomDTO.setRoomKeyNumber(room.getRoomKeyNumber());
        roomDTO.setIsUsable(room.getIsUsable());
        roomDTO.setBadgeNumber(room.getBadgeNumber());
        roomDTO.setFridgeKeyNumber(room.getFridgeKeyNumber());
        roomDTO.setIsReserved(room.getIsReserved());

        //mappage Many to One
        roomDTO.setIdFloor(FloorMapper.mapToFloorDTO(room.getIdFloor()));


        // Retourne le DTO RoomDTO
        return roomDTO;
    }



    // Méthode pour mapper un DTO RoomDTO vers une entité Room
    public static Room mapToRoomEntity(RoomDTO roomDto) {

        // créer un objet Room
        Room room = new Room();

        // Affecte les champs du DTO RoomDTO à l'entité Room
        room.setId(roomDto.getId());
        room.setRoomNumber(roomDto.getRoomNumber());
        room.setRoomKeyNumber(roomDto.getRoomKeyNumber());
        room.setIsUsable(roomDto.getIsUsable());
        room.setBadgeNumber(roomDto.getBadgeNumber());
        room.setFridgeKeyNumber(roomDto.getFridgeKeyNumber());
        room.setIsReserved(roomDto.getIsReserved());

        //mappage Many to One
        room.setIdFloor(FloorMapper.mapToFloorEntity(roomDto.getIdFloor()));

        // Retourne l'entité Room
        return room;
    }

}
