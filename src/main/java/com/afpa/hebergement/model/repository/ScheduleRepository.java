package com.afpa.hebergement.model.repository;


import com.afpa.hebergement.model.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    //recherche par id_schedule et id_intendant
    Optional<Schedule> findByIdAndIdIntendant_Id(Integer idSchedule, Integer idIntendant);

    //rechercher le(s) schedule(s) par son id_Intendant
    List<Schedule> findByIdIntendant_Id(Integer idIntendant);

    //supprimer par id_Intendant
    void deleteByIdIntendant_Id(Integer idIntendant);

    //rechercher un schedule par id intendant et id day
    Optional<Schedule> findByIdIntendant_IdAndIdDay_Id(Integer intendant, Integer day);

}
