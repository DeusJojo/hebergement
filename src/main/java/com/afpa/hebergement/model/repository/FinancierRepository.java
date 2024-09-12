package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.AppUser;
import com.afpa.hebergement.model.entity.Financier;
import com.afpa.hebergement.model.entity.Participate;
import com.afpa.hebergement.model.entity.SessionFormation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FinancierRepository extends JpaRepository<Financier, Integer> {
    Optional<Financier> findByParticipates_idSession(SessionFormation session);
    Optional<Financier> findByParticipates_idUser(AppUser user);
}
