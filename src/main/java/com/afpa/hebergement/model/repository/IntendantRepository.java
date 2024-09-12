package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.Intendant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface IntendantRepository extends JpaRepository<Intendant, Integer> {

    //rechercher les intendants d'un centre afpa
    List<Intendant> findByIdUser_IdAfpaCenter_Id(Integer idAfpaCenter);

}
