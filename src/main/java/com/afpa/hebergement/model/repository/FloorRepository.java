package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.Floor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FloorRepository extends JpaRepository<Floor, Integer> {

    //rechercher les étages par centre afpa
    List<Floor> findByIdAfpaCenter_Id(Integer idAfpaCenter);

    // rechercher les etages par centre afpa et par numéro de chambre
    Optional<Floor> findByNumberFloorAndIdAfpaCenter_Id(Integer numberFloor, Integer idAfpaCenter);
}
