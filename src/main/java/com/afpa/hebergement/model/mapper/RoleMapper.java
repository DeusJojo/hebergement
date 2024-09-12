package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.RoleDTO;
import com.afpa.hebergement.model.entity.Role;

public class RoleMapper {


    // Constructeur privé pour empêcher l'instanciation de cette classe
    private RoleMapper() {
    }

    // Méthode pour mapper une entité Role vers un DTO RoleDTO
    public static RoleDTO mapToRoleDTO(Role role) {

        //créer un objet roleDTO
        RoleDTO roleDTO = new RoleDTO();

        // Affecte les champs de l'entité Role au DTO RoleDTO
        roleDTO.setId(role.getId());
        roleDTO.setWordingRole(role.getWordingRole());

        // Retourne le DTO RoleDTO
        return roleDTO;
    }


    // Méthode pour mapper un DTO RoleDTO vers une entité Role
    public static Role mapToRoleEntity(RoleDTO roleDto) {

        //créer un objet role
        Role role = new Role();

        // Affecte les champs de l'entité RoleDTO à l'entité Role
        role.setId(roleDto.getId());
        role.setWordingRole(roleDto.getWordingRole());

        // Retourne l'entité Role
        return role;
    }

}

