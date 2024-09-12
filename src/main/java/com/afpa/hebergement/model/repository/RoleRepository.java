package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.Role;
import com.afpa.hebergement.model.enum_role.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    // Déclaration de la méthode findByWordingRole pour chercher un role
    Optional<Role> findByWordingRole(RoleType wordingRole);

}
