package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.ReservationDTO;
import com.afpa.hebergement.model.entity.Reservation;

public class ReservationMapper {

    // Constructeur privé pour empêcher l'instanciation de cette classe
    private ReservationMapper() {
    }

    // Méthode pour mapper une entité Reservation vers un DTO ReservationDTO
    public static ReservationDTO mapToReservationDTO(Reservation reservation) {

        // Créer un objet reservationDTO
        ReservationDTO reservationDTO = new ReservationDTO();

        // Affecte les champs de l'entité Reservation au DTO ReservationDTO
        reservationDTO.setId(reservation.getId());
        reservationDTO.setStartDate(reservation.getStartDate());
        reservationDTO.setEndDate(reservation.getEndDate());
        reservationDTO.setReservationDate(reservation.getReservationDate());

        //mappage des many to one
        reservationDTO.setIdRoom(RoomMapper.mapToRoomDTO(reservation.getIdRoom()));
        reservationDTO.setIdReservationMotive(ReservationMotiveMapper.mapToReservationMotiveDTO(reservation.getIdReservationMotive()));

        // Retourne le DTO ReservationDTO
        return reservationDTO;
    }


    // Méthode pour mapper un DTO ReservationDTO vers une entité Reservation
    public static Reservation mapToReservationEntity(ReservationDTO reservationDto) {

        // Créer un objet reservation
        Reservation reservation = new Reservation();

        // Affecte les champs du DTO ReservationDTO à l'entité Reservation
        reservation.setId(reservationDto.getId());
        reservation.setStartDate(reservationDto.getStartDate());
        reservation.setEndDate(reservationDto.getEndDate());
        reservation.setReservationDate(reservationDto.getReservationDate());

        //mappage des many to one
        reservation.setIdRoom(RoomMapper.mapToRoomEntity(reservationDto.getIdRoom()));
        reservation.setIdReservationMotive(ReservationMotiveMapper.mapToReservationMotiveEntity(reservationDto.getIdReservationMotive()));

        // Retourne l'entité Reservation
        return reservation;
    }


}
