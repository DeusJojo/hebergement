package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentTypeRepository extends JpaRepository<PaymentType,Integer> {
    Optional<PaymentType> findByWordingPaymentType(String name);
}
