package com.afpa.hebergement.service.entity_service;

import com.afpa.hebergement.model.dto.RoomDTO;
import com.afpa.hebergement.service.GenericService;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface RoomService extends GenericService<RoomDTO, Integer> {

    //méthode pour récupérer les chambres par afpa center
    List<RoomDTO> getAllByAfpaCenter(Integer idAfpaCenter);

    //Méthode pour récupérer les chambres par centre afpa avec pagination
    Page<RoomDTO> getAllByAfpaCenter(Integer idAfpaCenter, Integer page, Integer size);

    //récupérer les chambres qui sont réservées et par centre afpa
    List<RoomDTO> getByReservedRoomAndIdAfpaCenter(Integer idAfpaCenter);

    //récupérer les chambres libres par centre afpa
    List<RoomDTO> getByAvailableRoomAndIdAfpaCenter(Integer idAfpaCenter);

    //récupérer les chambres occupées par centre afpa
    List<RoomDTO> getByOccupiedRoomAndIdAfpaCenter(Integer idAfpaCenter);

    //récupérer les chambres réservées aux femmes par centre afpa
    List<RoomDTO> getByWomanRoomAndIdAfpaCenter(Integer idAfpaCenter);

    //récupérer une chambre par numéro de chambre et centre afpa
    Optional<RoomDTO> getByRoomNumberAndIdAfpaCenter(String roomNumber, Integer idAfpaCenter);

}
