package com.afpa.hebergement.model.repository;


import com.afpa.hebergement.model.entity.Room;
import com.afpa.hebergement.model.entity.Work;
import com.afpa.hebergement.model.entity.WorkType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkRepository extends JpaRepository<Work, Integer> {

    //rechercher si des travaux existent déjà pour les memes dates, la meme chambre et le meme type de travaux
    Optional<Work> findByStartWorkAndEndWorkAndIdWorkTypeAndIdRoom(LocalDate startDate, LocalDate endDate, WorkType idWorkType, Room idRoom);

    //rechercher les chambres en travaux pour un centre afpa
    List<Work> findByIdRoom_IdFloor_IdAfpaCenter_Id(Integer idAfpaCenter);
}
