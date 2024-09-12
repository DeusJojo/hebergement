package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.*;
import com.afpa.hebergement.model.dto.RoleDTO;
import com.afpa.hebergement.model.entity.Role;
import com.afpa.hebergement.model.mapper.RoleMapper;
import com.afpa.hebergement.model.repository.RoleRepository;
import com.afpa.hebergement.service.entity_service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;



@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {


    private final RoleRepository roleRepository;


    @Override
    public RoleDTO create(RoleDTO roleDto) {

        // Vérifier si le rôle existe déjà
        Optional<Role> existingRole = roleRepository.findByWordingRole(roleDto.getWordingRole());

        if (existingRole.isPresent()) {
            // Lancer une exception si le rôle existe déjà
            throw new DuplicateException("Role already exists");
        }

        // Mappe le DTO en entité Role
        Role role = RoleMapper.mapToRoleEntity(roleDto);

        try {
            // Sauvegarde l'entité Role dans le repository
            Role savedRole = roleRepository.save(role);
            // Mappe l'entité Role sauvegardée en DTO et la retourne
            return RoleMapper.mapToRoleDTO(savedRole);

        } catch (DataIntegrityViolationException e) {
            // Capturer une exception de violation d'intégrité des données
            throw new CreationException("Error occurred while creating role");
        }

    }


    @Override
    public List<RoleDTO> getAll() {

        // Récupère toutes les entités Role du repository
        List<Role> roles = roleRepository.findAll();

        // Vérifie si la liste est vide
        if (roles.isEmpty()) {
            // Lance une ResourceNotFoundException si aucune entité n'est trouvée
            throw new NoContentException("No roles found");
        }

        try {
            // Mappe les entités Role en DTO et retourne la liste
            return roles.stream()
                    .map(RoleMapper::mapToRoleDTO)
                    .toList();

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while getting all roles");
        }
    }


    @Override
    public Optional<RoleDTO> getById(Integer id) {

        // Cherche une entité Role par son identifiant
        Role findRole = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + id));

        try {
            // Convertit l'entité Role en RoleDTO par le RoleMapper
            RoleDTO roleDTO = RoleMapper.mapToRoleDTO(findRole);

            // Retourne le RoleDTO encapsulé dans un Optional
            return Optional.of(roleDTO);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while getting role with ID: " + id);
        }
    }


    @Override
    public Optional<RoleDTO> update(Integer id, RoleDTO roleDto) {

        // Cherche l'entité Role à mettre à jour par son identifiant
        Role roleToUpdate = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant ID not found: " + id));

        try {
            // Met à jour le champ wordingRole de l'entité Role avec la valeur du DTO
            roleToUpdate.setWordingRole(roleDto.getWordingRole());

            // Sauvegarde l'entité Role mise à jour dans le repository
            Role updatedRole = roleRepository.save(roleToUpdate);

            //Mappe l'entité Role mise à jour en DTO et la retourne (encapsulée) dans un Optional
            return Optional.of(RoleMapper.mapToRoleDTO(updatedRole));

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while update role with ID: " + id);
        }

    }


    @Override
    public void deleteById(Integer id) {

        // Tente de trouver l'entité Role par son identifiant
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
        try {
            // Supprime l'entité Role trouvée
            roleRepository.delete(role);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("An unexpected error occurred while deleting role with id: " + id);
        }
    }


}
