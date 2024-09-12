package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.Formation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormationRepository extends JpaRepository<Formation, Integer> {
}
