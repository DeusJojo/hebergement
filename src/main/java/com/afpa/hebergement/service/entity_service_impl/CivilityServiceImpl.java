package com.afpa.hebergement.service.entity_service_impl;


import com.afpa.hebergement.exception.*;
import com.afpa.hebergement.model.dto.CivilityDTO;
import com.afpa.hebergement.model.entity.Civility;
import com.afpa.hebergement.model.mapper.CivilityMapper;
import com.afpa.hebergement.model.repository.CivilityRepository;
import com.afpa.hebergement.service.entity_service.CivilityService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class CivilityServiceImpl implements CivilityService {


    private final CivilityRepository civilityRepository;


    @Override
    public CivilityDTO create(CivilityDTO civilityDto) {

        // Vérifier si le rôle existe déjà
        Optional<Civility> existingCivility = civilityRepository.findByWordingCivility(civilityDto.getWordingCivility());

        if (existingCivility.isPresent()) {
            // Lancer une exception si la civilité existe déjà
            throw new DuplicateException("Civility already exists");
        }

        // Mappe le DTO en entité Civility
        Civility civility = CivilityMapper.mapToCivilityEntity(civilityDto);

        try {
            // Sauvegarde l'entité Civility dans le repository
            Civility savedCivility = civilityRepository.save(civility);

            // Mappe l'entité Civility sauvegardée en DTO et la retourne
            return CivilityMapper.mapToCivilityDTO(savedCivility);

        } catch (DataIntegrityViolationException e) {
            // Capturer une exception de violation d'intégrité des données
            throw new CreationException("Error occurred while creating civility");
        }

    }


    @Override
    public List<CivilityDTO> getAll() {

        // Récupère toutes les entités Role du repository
        List<Civility> civilities = civilityRepository.findAll();

        // Vérifie si la liste est vide
        if (civilities.isEmpty()) {
            // Lance une ResourceNotFoundException si aucune entité n'est trouvée
            throw new NoContentException("No civilities found");
        }

        try {
            // Récupère toutes les entités Civility du repository et les mappe en DTO
            return civilities.stream()
                    .map(CivilityMapper::mapToCivilityDTO)
                    .toList();

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while getting all civilities");
        }
    }


    @Override
    public Optional<CivilityDTO> getById(Integer id) {

        // Cherche une entité Civility par son identifiant
        Civility findCivility = civilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Civility not found with ID: " + id));

        try {
            // Convertit l'entité Civility en CivilityDTO par le CivilityMapper
            CivilityDTO civilityDTO = CivilityMapper.mapToCivilityDTO(findCivility);

            // Retourne le CivilityDTO encapsulé dans un Optional
            return Optional.of(civilityDTO);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred to recover civility with ID: " + id);
        }
    }


    @Override
    public Optional<CivilityDTO> update(Integer id, CivilityDTO civilityDto) {

        // Cherche l'entité Civility à mettre à jour par son identifiant
        Civility civilityToUpdate = civilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Civility ID not found: " + id));

        try {
            // Met à jour le champ wordingCivility de l'entité Civility avec la valeur du DTO
            civilityToUpdate.setWordingCivility(civilityDto.getWordingCivility());

            // Sauvegarde l'entité Civility mise à jour dans le repository
            Civility updatedCivility = civilityRepository.save(civilityToUpdate);

            // Mappe et retourne l'entité Civility mise à jour en DTO encapsulé dans Optional
            return Optional.of(CivilityMapper.mapToCivilityDTO(updatedCivility));

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while updating the civility with ID: " + id);
        }
    }


    @Override
    public void deleteById(Integer id) {

        Civility civility = civilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Civility not found with id: " + id));

        try {
            // Supprime l'entité Civility par son identifiant
            civilityRepository.delete(civility);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("An unexpected error occurred while deleting civility with id: " + id);
        }
    }

}

