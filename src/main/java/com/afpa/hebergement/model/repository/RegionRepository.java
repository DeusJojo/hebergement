package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Integer> {
    Optional<Region> findByRegionName(String regionName);
}
