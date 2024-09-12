package com.afpa.hebergement.model.mapper;


import com.afpa.hebergement.model.dto.AppointmentDTO;
import com.afpa.hebergement.model.entity.Appointment;

public class AppointmentMapper {

    // Constructeur privé pour empêcher l'instanciation de cette classe
    private AppointmentMapper() {

    }


    //Méthode pour mapper une entité Appointment vers un DTO AppointmentDTO.
    public static AppointmentDTO mapToAppointmentDTO(Appointment appointment) {

        // Crée un objet AppointmentDTO
        AppointmentDTO appointmentDTO = new AppointmentDTO();

        // Affecte les champs de l'entité Appointment au DTO AppointmentDTO
        appointmentDTO.setId(appointment.getId());
        appointmentDTO.setDateAppointment(appointment.getDateAppointment());
        appointmentDTO.setReason(appointment.getReason());

        // Mappage de Many to One
        appointmentDTO.setIdIntendant(IntendantMapper.mapToIntendantDTO(appointment.getIdIntendant()));
        appointmentDTO.setIdUser(AppUserMapper.mapToAppUserDTO(appointment.getIdUser()));

        // Retourne le DTO AppointmentDTO
        return appointmentDTO;
    }


    //Méthode pour mapper un DTO AppointmentDTO vers une entité Appointment.
    public static Appointment mapToAppointmentEntity(AppointmentDTO appointmentDto) {

        // Crée un objet Appointment
        Appointment appointment = new Appointment();

        // Affecte les champs du DTO AppointmentDTO à l'entité Appointment
        appointment.setId(appointmentDto.getId());
        appointment.setDateAppointment(appointmentDto.getDateAppointment());
        appointment.setReason(appointmentDto.getReason().trim());

        // Mappage de Many to One
        appointment.setIdIntendant(IntendantMapper.mapToIntendantEntity(appointmentDto.getIdIntendant()));
        appointment.setIdUser(AppUserMapper.mapToAppUser(appointmentDto.getIdUser()));

        // Retourne l'entité Appointment
        return appointment;
    }

}
