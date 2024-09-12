package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.*;
import com.afpa.hebergement.model.dto.IntendantDTO;
import com.afpa.hebergement.model.entity.AfpaCenter;
import com.afpa.hebergement.model.entity.Intendant;
import com.afpa.hebergement.model.mapper.IntendantMapper;
import com.afpa.hebergement.model.repository.AfpaCenterRepository;
import com.afpa.hebergement.model.repository.IntendantRepository;
import com.afpa.hebergement.service.entity_service.IntendantService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class IntendantServiceImpl implements IntendantService {


    private final IntendantRepository intendantRepository;
    private final AfpaCenterRepository afpaCenterRepository;


    // Récupérer tous les intendants d'un centre Afpa
    public List<IntendantDTO> getAllIntendantsByAfpaCenter(Integer afpaCenterId) {

        // Recherche l'id du centre afpa dans le repository
        AfpaCenter afpaCenter = afpaCenterRepository.findById(afpaCenterId)
                .orElseThrow(() -> new ResourceNotFoundException("Afpa Center ID not found: " + afpaCenterId));

        // Récupération des intendants d'un centre afpa
        List<Intendant> intendantList = intendantRepository.findByIdUser_IdAfpaCenter_Id(afpaCenter.getId());

        // Vérifie si la liste est vide
        if (intendantList.isEmpty()) {
            // Lance une NoContentException si aucune entité n'est trouvée
            throw new NoContentException("No intendants found");
        }

        try {
            // Mappe les entités Intendant en DTO et retourne la liste
            return intendantList.stream()
                    .map(IntendantMapper::mapToIntendantDTO)
                    .toList();

        } catch (Exception e) {
            throw new InternalServerException("Error occurred while getting intendants by Afpa Center ID");
        }
    }


    @Override
    public Optional<IntendantDTO> getById(Integer id) {

        // Cherche une entité Intendant par son identifiant
        Intendant findIntendant = intendantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Intendant not found with ID: " + id));

        try {
            // Convertit l'entité Intendant en IntendantDTO par le IntendantMapper
            IntendantDTO intendantDTO = IntendantMapper.mapToIntendantDTO(findIntendant);

            // Retourne le IntendantDTO encapsulé dans un Optional
            return Optional.of(intendantDTO);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred to recover intendant with ID: " + id);
        }
    }


    @Override
    public Optional<IntendantDTO> update(Integer id, IntendantDTO intendantDto) {

        // Cherche l'entité Intendant à mettre à jour par son identifiant
        Intendant intendantToUpdate = intendantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Intendant ID not found: " + id));

        // Met à jour les champs de l'entité Intendant avec les valeurs du DTO (champs pouvant être null)
        intendantToUpdate.setStartDateIntendant(intendantDto.getStartDateIntendant());
        intendantToUpdate.setEndDateIntendant(intendantDto.getEndDateIntendant());

        try {

            // Sauvegarde l'entité Intendant mise à jour dans le repository
            Intendant updatedIntendant = intendantRepository.save(intendantToUpdate);

            // Mappe l'entité Intendant mise à jour en DTO et la retourne (encapsulée) dans un Optional
            return Optional.of(IntendantMapper.mapToIntendantDTO(updatedIntendant));

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while updating the intendant with ID: " + id);
        }
    }


    @Override
    public void deleteById(Integer id) {

        // Tente de trouver l'entité Intendant par son identifiant
        Intendant intendant = intendantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Intendant not found with id: " + id));

        try {
            // Supprime l'entité Intendant trouvée
            intendantRepository.delete(intendant);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("An unexpected error occurred while deleting the intendant with id: " + id);
        }
    }

}
