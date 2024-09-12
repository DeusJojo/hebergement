package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.AfpaCenter;
import com.afpa.hebergement.model.entity.AppUser;
import com.afpa.hebergement.model.entity.SessionFormation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

    //rechercher un utilisateur par son nom
    Optional<AppUser> findByName(String username);

    //recherche d'un utilisateur par son numéro de bénéficiaire
    Optional<AppUser> findByBeneficiaryNumber(String beneficiaryNumber);

    //recherche d'un utilisateur par son centre afpa
    List<AppUser> findByIdAfpaCenter(AfpaCenter idAfpaCenter);

    //rechercher tous les users par centre afpa avec pagination
    Page<AppUser> findByIdAfpaCenter(AfpaCenter afpaCenter, Pageable pageable);

    //rechercher un utilisateur par son email
    Optional<AppUser> findByEmail(String email);

    //rechercher un utilisateur par son nom, prénom, date de naissance
    Optional<AppUser> findByNameAndFirstnameAndBirthdate(String name, String firstname, LocalDate birthdate);

    //rechercher tous les utilisateurs par session de formation
    Optional<Set<AppUser>> findAllByParticipates_IdSession(SessionFormation sessionFormation);

    boolean existsByBeneficiaryNumber(String number);

    boolean existsByEmail(String email);


}
