package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.Participate;
import com.afpa.hebergement.model.entity.SessionFormation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface ParticipateRepository extends JpaRepository<Participate, Integer> {

    public Participate findByIdSession(SessionFormation Session);
    public Set<Participate> findAllByIdSession(SessionFormation session);

    //récupérer la dernière session de formation d'un utilisateur par id user et ordre décroissant de l'ID de participation
    Optional<Participate> findFirstByIdUser_IdOrderByIdDesc(Integer idUser);

}
