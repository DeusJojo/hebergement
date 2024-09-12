package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.SessionFormation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionFormationRepository extends JpaRepository<SessionFormation, Integer> {
}
