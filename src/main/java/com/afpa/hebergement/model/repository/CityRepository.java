package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Integer> {
    Optional<City> findByInseeCodeAndPostcode(String inseeCode, String postCode);
}
