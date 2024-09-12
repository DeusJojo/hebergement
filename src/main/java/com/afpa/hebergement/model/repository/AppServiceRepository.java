package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.AppService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppServiceRepository extends JpaRepository<AppService, Integer> {

    List<AppService> findAllByAfpaCenters_Id(Integer idAfpaCenter);

    //rechercher un service par son nom de service
    Optional<AppService> findByWordingService(String serviceName);
}
