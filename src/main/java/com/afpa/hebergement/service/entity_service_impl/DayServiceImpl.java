package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.*;
import com.afpa.hebergement.model.dto.DayDTO;
import com.afpa.hebergement.model.entity.Day;
import com.afpa.hebergement.model.mapper.DayMapper;
import com.afpa.hebergement.model.repository.DayRepository;
import com.afpa.hebergement.service.entity_service.DayService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class DayServiceImpl implements DayService {

    private final DayRepository dayRepository;


    @Override
    public DayDTO create(DayDTO dayDto) {

        // Vérifier si le jour existe déjà
        Optional<Day> existingDay = dayRepository.findByWordingDay(dayDto.getWordingDay());

        if (existingDay.isPresent()) {
            // Lancer une exception si le jour existe déjà
            throw new DuplicateException("Day already exists");
        }

        // Mappe le DTO en entité Day
        Day day = DayMapper.mapToDayEntity(dayDto);

        try {
            // Sauvegarde l'entité Day dans le repository
            Day savedDay = dayRepository.save(day);

            // Mappe l'entité Day sauvegardée en DTO et la retourne
            return DayMapper.mapToDayDTO(savedDay);

        } catch (DataIntegrityViolationException e) {
            // Capturer une exception de violation d'intégrité des données
            throw new CreationException("Error occurred while creating day");
        }
    }


    @Override
    public List<DayDTO> getAll() {

        // Récupère toutes les entités Day du repository
        List<Day> days = dayRepository.findAll();

        // Vérifie si la liste est vide
        if (days.isEmpty()) {
            // Lance une NoContentException si aucune entité n'est trouvée
            throw new NoContentException("No days found");
        }

        try {
            // Mappe les entités Day en DTO et retourne la liste
            return days.stream()
                    .map(DayMapper::mapToDayDTO)
                    .toList();

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while getting all days");
        }
    }


    @Override
    public Optional<DayDTO> getById(Integer id) {

        // Cherche une entité Day par son identifiant
        Day findDay = dayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Day not found with ID: " + id));

        try {
            // Convertit l'entité Day en DayDTO par le DayMapper
            DayDTO dayDTO = DayMapper.mapToDayDTO(findDay);

            // Retourne le DayDTO encapsulé dans un Optional
            return Optional.of(dayDTO);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while getting the day");
        }
    }


    @Override
    public Optional<DayDTO> update(Integer id, DayDTO dayDto) {

        // Cherche l'entité Day à mettre à jour par son identifiant
        Day dayToUpdate = dayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Day ID not found: " + id));

        try {

            // Met à jour le champ wordingDay de l'entité Day avec la valeur du DTO
            dayToUpdate.setWordingDay(dayDto.getWordingDay());

            // Sauvegarde l'entité Day mise à jour dans le repository
            Day updatedDay = dayRepository.save(dayToUpdate);

            // Mappe l'entité Day mise à jour en DTO et la retourne (encapsulée) dans un Optional
            return Optional.of(DayMapper.mapToDayDTO(updatedDay));

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while updating the day");
        }
    }


    @Override
    public void deleteById(Integer id) {

        // Tente de trouver l'entité Day par son identifiant
        Day day = dayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Day not found with id: " + id));

        try {
            // Supprime l'entité Day trouvée
            dayRepository.delete(day);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("An unexpected error occurred while deleting day with id: " + id);
        }
    }

}
