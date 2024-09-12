package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Integer> {


    //rechercher les chambres d'un centre afpa
    List<Room> findByIdFloor_IdAfpaCenter_Id(Integer idAfpaCenter);

    //rechercher tous les users par centre afpa avec pagination
    Page<Room> findByIdFloor_IdAfpaCenter_Id(Integer idAfpaCenter, Pageable pageable);

    //Rechercher les chambres qui sont réservées et par centre afpa
    List<Room> findByIsReservedTrueAndIdFloor_IdAfpaCenter_Id(Integer afpaCenter);

    //Rechercher les chambres libres par centre afpa = true
    List<Room> findByIsUsableTrueAndIdFloor_IdAfpaCenter_Id(Integer afpaCenter);

    //Rechercher les chambres occupées par centre afpa = false
    List<Room> findByIsUsableFalseAndIdFloor_IdAfpaCenter_Id(Integer afpaCenter);

    //Rechercher les chambres réservées aux femmes par centre afpa
    List<Room> findByIdFloor_IsForWomenTrueAndIdFloor_IdAfpaCenter_Id(Integer afpaCenter);

    //Rechercher une chambre par numéro de chambre et id centre afpa
    Optional<Room> findByRoomNumberAndIdFloor_IdAfpaCenter_Id(String roomNumber, Integer idAfpaCenter);

    //rechercher une chambre par numéro de chambre et id floor
    Optional<Room> findByRoomNumberAndIdFloor_Id(String roomNumber, Integer floorId);

}

