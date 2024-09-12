package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.Guarantee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GuaranteeRepository extends JpaRepository<Guarantee, Integer> {
    Optional<Guarantee> findByIdGuaranteeType_IdAndIdAfpaCenter_Id(Integer idGuaranteeType, Integer idAfpaCenter);
    List<Guarantee> findAllByIdAfpaCenter_IdOrderByIdAsc(Integer idAfpaCenter);
}
