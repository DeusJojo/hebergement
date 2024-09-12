package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.Rent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RentRepository extends JpaRepository<Rent, Integer> {

    List<Rent> findAllByIdAfpaCenter_Id(Integer idAfpaCenter);

    //rechercher un rent par sa fréquence et par centre afpa
    Optional<Rent> findByFrequencyAndIdAfpaCenter_Id(String frequency, Integer idAfpaCenter);
}
