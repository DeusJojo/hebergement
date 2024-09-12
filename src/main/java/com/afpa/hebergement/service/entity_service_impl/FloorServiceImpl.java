package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.*;
import com.afpa.hebergement.model.dto.FloorDTO;
import com.afpa.hebergement.model.entity.AfpaCenter;
import com.afpa.hebergement.model.entity.Floor;
import com.afpa.hebergement.model.mapper.FloorMapper;
import com.afpa.hebergement.model.repository.AfpaCenterRepository;
import com.afpa.hebergement.model.repository.FloorRepository;
import com.afpa.hebergement.service.entity_service.FloorService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;



@Service
@AllArgsConstructor
public class FloorServiceImpl implements FloorService {


    private final FloorRepository floorRepository;
    private final AfpaCenterRepository afpaCenterRepository;



    // Méthode pour créer un nouvel étage
    @Override
    public FloorDTO create(FloorDTO floorDto) {

        // Vérifier si le centre Afpa existe
        AfpaCenter afpaCenter = afpaCenterRepository.findById(floorDto.getIdAfpaCenter().getId())
                .orElseThrow(() -> new ResourceNotFoundException("AfpaCenter not found with ID: " + floorDto.getIdAfpaCenter().getId()));

        // Vérifier si l'étage existe déjà par un champ unique (numéro d'étage et id afpa center)
        Optional<Floor> existingFloor = floorRepository.findByNumberFloorAndIdAfpaCenter_Id(
               floorDto.getNumberFloor(),
               afpaCenter.getId()
        );

        if (existingFloor.isPresent()) {
            // Lancer une exception si l'étage existe déjà
            throw new DuplicateException("Floor already exists with floor number: " + floorDto.getNumberFloor());
        }

        try {
            // Mappe le DTO en entité Floor
            Floor floor = FloorMapper.mapToFloorEntity(floorDto);

            // Sauvegarde l'entité Floor dans le repository
            Floor savedFloor = floorRepository.save(floor);

            // Mappe l'entité Floor sauvegardée en DTO et la retourne
            return FloorMapper.mapToFloorDTO(savedFloor);

        } catch (DataIntegrityViolationException e) {
            // Capturer une exception de violation d'intégrité des données
            throw new CreationException("Error occurred while creating floor");
        }
    }


    @Override
    public Optional<FloorDTO> getById(Integer id) {

        // Cherche une entité Floor par son identifiant
        Floor findFloor = floorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Floor not found with ID: " + id));

        try {
            // Convertit l'entité Floor en FloorDTO par le FloorMapper
            FloorDTO floorDTO = FloorMapper.mapToFloorDTO(findFloor);

            // Retourne le FloorDTO encapsulé dans un Optional
            return Optional.of(floorDTO);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred to recover floor with ID: " + id);
        }
    }


    // Méthode pour obtenir les étages par centre Afpa via les chambres
    public List<FloorDTO> getFloorsByAfpaCenter(Integer idAfpaCenter) {

        // Rechercher le centre Afpa dans le repository
        AfpaCenter afpaCenter = afpaCenterRepository.findById(idAfpaCenter)
                .orElseThrow(() -> new ResourceNotFoundException("AfpaCenter not found with ID: " + idAfpaCenter));

        // Chercher les étages associés au centre Afpa
        List<Floor> floorsList = floorRepository.findByIdAfpaCenter_Id(afpaCenter.getId());

        // Vérifie si la liste est vide
        if (floorsList.isEmpty()) {
            // Lance une ResourceNotFoundException si aucune entité n'est trouvée
            throw new NoContentException("No floors found");
        }

        try {
            // Mappe les entités Floor en DTO et retourne la liste
            return floorsList.stream()
                    .map(FloorMapper::mapToFloorDTO)
                    .toList();

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while getting floor by ID Afpa center: " + idAfpaCenter);
        }
    }



    @Override
    public Optional<FloorDTO> update(Integer id, FloorDTO floorDto) {

        // Cherche l'entité Floor à mettre à jour par son identifiant
        Floor floorToUpdate = floorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Floor ID not found: " + id));

        try {
            // Met à jour les champs de l'entité Floor avec les valeurs du DTO
            floorToUpdate.setNumberFloor(floorDto.getNumberFloor());
            floorToUpdate.setIsForWomen(floorDto.getIsForWomen());

            // Sauvegarde l'entité Floor mise à jour dans le repository
            Floor updatedFloor = floorRepository.save(floorToUpdate);

            // Mappe et retourne l'entité Floor mise à jour en DTO encapsulé dans Optional
            return Optional.of(FloorMapper.mapToFloorDTO(updatedFloor));

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while updating the floor with ID: " + id);
        }
    }


    @Override
    public void deleteById(Integer id) {

        // Tente de trouver l'entité Floor par son identifiant
        Floor floor = floorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Floor not found with id: " + id));
        try {
            // Supprime l'entité Floor trouvée
            floorRepository.delete(floor);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("An unexpected error occurred while deleting floor with id: " + id);
        }
    }

}
