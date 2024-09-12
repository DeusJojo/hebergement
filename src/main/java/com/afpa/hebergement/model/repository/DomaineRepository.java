package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.Domaine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DomaineRepository extends JpaRepository<Domaine, Integer> {
}
