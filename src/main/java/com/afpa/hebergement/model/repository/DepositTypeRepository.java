package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.DepositType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface DepositTypeRepository extends JpaRepository<DepositType, Integer> {
    public Optional<DepositType> findByWordingDepositType(String name);
}
