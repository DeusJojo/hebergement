package com.afpa.hebergement.service.entity_service;

import com.afpa.hebergement.model.dto.ReservationDTO;
import com.afpa.hebergement.service.GenericService;

import java.util.List;

public interface ReservationService extends GenericService<ReservationDTO, Integer> {

    //méthode pour récupérer les reservations d'un centre afpa
    List<ReservationDTO> getReservationsByAfpaCenter(Integer idAfpaCenter);

    //créer une reservation par id room et id reservation motive
    ReservationDTO create (ReservationDTO reservationDto, Integer idRoom, Integer idReservationMotive);
}
