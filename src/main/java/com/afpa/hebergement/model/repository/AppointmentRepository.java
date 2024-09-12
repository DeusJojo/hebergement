package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {


    //rechercher un rendez-vous par la date/heure, id intendant  et l'id user
    Optional<Appointment> findByDateAppointmentAndIdIntendantAndIdUser(LocalDateTime dateAppointment, Intendant idIntendant, AppUser idUser);

    //rechercher les rendez-vous par centre afpa
    List<Appointment> findByIdUser_IdAfpaCenter_Id(Integer idAfpaCenter);

    //rechercher tous les rendez-vous par centre afpa avec pagination
    Page<Appointment> findByIdUser_IdAfpaCenter_Id(Integer idAfpaCenter, Pageable pageable);

    //rechercher les rendez-vous par id intendant
    List<Appointment> findByIdIntendant_Id(Integer idIntendant);

    //rechercher les rendez-vous par id User
    List<Appointment> findByIdUser_Id(Integer idUser);

    //rechercher les rendez-vous par date
    List<Appointment> findByDateAppointmentBetween(LocalDateTime startDate, LocalDateTime endDate);

}
