package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.Civility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CivilityRepository extends JpaRepository<Civility, Integer> {

    // Déclaration de la méthode findByWordingCivility pour chercher un role
    Optional<Civility> findByWordingCivility(String wordingCivility);

}
