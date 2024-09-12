package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.ReservationMotive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationMotiveRepository extends JpaRepository<ReservationMotive, Integer> {

    Optional<ReservationMotive> findByMotive(String motive);
}
