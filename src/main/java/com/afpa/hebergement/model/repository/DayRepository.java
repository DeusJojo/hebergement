package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.Day;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DayRepository  extends JpaRepository<Day, Integer> {

    Optional<Day> findByWordingDay(String day);

}
