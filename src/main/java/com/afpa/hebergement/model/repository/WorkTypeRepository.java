package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.WorkType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkTypeRepository extends JpaRepository<WorkType, Integer> {

    Optional<WorkType> findByWordingWorkType(String name);
}
