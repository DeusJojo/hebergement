package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.ReservationMotiveDTO;
import com.afpa.hebergement.model.entity.ReservationMotive;


public class ReservationMotiveMapper {

    // Constructeur privé pour empêcher l'instanciation de cette classe
    private ReservationMotiveMapper() {
    }

    // Méthode pour mapper une entité ReservationMotive vers un DTO ReservationMotiveDTO
    public static ReservationMotiveDTO mapToReservationMotiveDTO(ReservationMotive reservationMotive) {

        // Crée un objet ReservationMotiveDTO
        ReservationMotiveDTO reservationMotiveDTO = new ReservationMotiveDTO();

        // Affecte les champs de l'entité ReservationMotive au DTO ReservationMotiveDTO
        reservationMotiveDTO.setId(reservationMotive.getId());
        reservationMotiveDTO.setMotive(reservationMotive.getMotive());

        // Retourne le DTO ReservationMotiveDTO
        return reservationMotiveDTO;
    }


    // Méthode pour mapper un DTO ReservationMotiveDTO vers une entité ReservationMotive
    public static ReservationMotive mapToReservationMotiveEntity(ReservationMotiveDTO reservationMotiveDto) {

        // Crée un objet ReservationMotive
        ReservationMotive reservationMotive = new ReservationMotive();

        // Affecte les champs de l'entité ReservationMotiveDTO à l'entité ReservationMotive
        reservationMotive.setId(reservationMotiveDto.getId());
        reservationMotive.setMotive(reservationMotiveDto.getMotive().toLowerCase().trim());

        // Retourne l'entité ReservationMotive
        return reservationMotive;
    }

}

