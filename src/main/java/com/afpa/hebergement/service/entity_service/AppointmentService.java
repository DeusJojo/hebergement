package com.afpa.hebergement.service.entity_service;

import com.afpa.hebergement.model.dto.AppointmentDTO;
import com.afpa.hebergement.service.GenericService;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AppointmentService extends GenericService<AppointmentDTO, Integer> {

    //récupérer tous les rendez-vous par centre afpa
    List<AppointmentDTO> getAllAppointmentsByAfpaCenter(Integer idAfpaCenter);

    //récupérer les appointments par centre afpa avec pagination
    Page<AppointmentDTO>  getAllAppointmentsByAfpaCenter(Integer idAfpaCenter, Integer page, Integer size);

    //récupérer les rendez-vous par intendant
    List<AppointmentDTO> getAppointmentsByIntendant(Integer idIntendant);

    //récupérer les rendez-vous par utilisateurs
    List<AppointmentDTO> getAppointmentsByUser(Integer idUser);

    //récupérer les rendez-vous par date
    List<AppointmentDTO> getAppointmentsByDate(String dateAppointment);

}
