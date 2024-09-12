package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.AfpaCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AfpaCenterRepository extends JpaRepository<AfpaCenter, Integer> {
    Optional<AfpaCenter> findByCenterName(String centerName);
    Optional<AfpaCenter> findByIdCity_CityName (String idCity);
}
