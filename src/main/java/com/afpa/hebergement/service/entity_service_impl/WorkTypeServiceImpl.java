package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.*;
import com.afpa.hebergement.model.dto.WorkTypeDTO;
import com.afpa.hebergement.model.entity.WorkType;
import com.afpa.hebergement.model.mapper.WorkTypeMapper;
import com.afpa.hebergement.model.repository.WorkTypeRepository;
import com.afpa.hebergement.service.entity_service.WorkTypeService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class WorkTypeServiceImpl implements WorkTypeService {


    private final WorkTypeRepository workTypeRepository;


    @Override
    public WorkTypeDTO create(WorkTypeDTO workTypeDto) {

        // Vérifier si le type de travail existe déjà
        Optional<WorkType> existingWorkType = workTypeRepository.findByWordingWorkType(workTypeDto.getWordingWorkType());

        if (existingWorkType.isPresent()) {
            // Lancer une exception si le type de travail existe déjà
            throw new DuplicateException("Work type already exists");
        }

        // Mappe le DTO en entité WorkType
        WorkType workType = WorkTypeMapper.mapToWorkTypeEntity(workTypeDto);

        try {
            // Sauvegarde l'entité WorkType dans le repository
            WorkType savedWorkType = workTypeRepository.save(workType);
            // Mappe l'entité WorkType sauvegardée en DTO et la retourne
            return WorkTypeMapper.mapToWorkTypeDTO(savedWorkType);

        } catch (DataIntegrityViolationException e) {
            // Capturer une exception de violation d'intégrité des données
            throw new CreationException("Error occurred while creating work type");
        }
    }


    @Override
    public List<WorkTypeDTO> getAll() {

        // Récupère toutes les entités WorkType du repository
        List<WorkType> workTypes = workTypeRepository.findAll();

        // Vérifie si la liste est vide
        if (workTypes.isEmpty()) {
            // Lance une ResourceNotFoundException si aucune entité n'est trouvée
            throw new NoContentException("No work types found");
        }

        try {
            // Mappe les entités WorkType en DTO et retourne la liste
            return workTypes.stream()
                    .map(WorkTypeMapper::mapToWorkTypeDTO)
                    .toList();

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while getting all work types");
        }
    }


    @Override
    public Optional<WorkTypeDTO> getById(Integer id) {

        // Cherche une entité WorkType par son identifiant
        WorkType findWorkType = workTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work type not found with ID: " + id));

        try {
            // Convertit l'entité WorkType en WorkTypeDTO par le WorkTypeMapper
            WorkTypeDTO workTypeDTO = WorkTypeMapper.mapToWorkTypeDTO(findWorkType);

            // Retourne le WorkTypeDTO encapsulé dans un Optional
            return Optional.of(workTypeDTO);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while getting work type by ID");
        }
    }


    @Override
    public Optional<WorkTypeDTO> update(Integer id, WorkTypeDTO workTypeDto) {

        // Cherche l'entité WorkType à mettre à jour par son identifiant
        WorkType workTypeToUpdate = workTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work type ID not found: " + id));

        try {
            // Met à jour le champ wordingWorkType de l'entité WorkType avec la valeur du DTO
            workTypeToUpdate.setWordingWorkType(workTypeDto.getWordingWorkType());

            // Sauvegarde l'entité WorkType mise à jour dans le repository
            WorkType updatedWorkType = workTypeRepository.save(workTypeToUpdate);

            // Mappe l'entité WorkType mise à jour en DTO et la retourne (encapsulée) dans un Optional
            return Optional.of(WorkTypeMapper.mapToWorkTypeDTO(updatedWorkType));

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while updating work type");
        }
    }


    @Override
    public void deleteById(Integer id) {

        // Tente de trouver l'entité WorkType par son identifiant
        WorkType workType = workTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work type not found with id: " + id));
        try {
            // Supprime l'entité WorkType trouvée
            workTypeRepository.delete(workType);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("An unexpected error occurred while deleting work type with id: " + id);
        }
    }

}

