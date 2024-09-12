package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.*;
import com.afpa.hebergement.model.dto.ReservationMotiveDTO;
import com.afpa.hebergement.model.entity.ReservationMotive;
import com.afpa.hebergement.model.mapper.ReservationMotiveMapper;
import com.afpa.hebergement.model.repository.ReservationMotiveRepository;

import com.afpa.hebergement.service.entity_service.ReservationMotiveService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class ReservationMotiveServiceImpl implements ReservationMotiveService {


    private final ReservationMotiveRepository reservationMotiveRepository;


    @Override
    public ReservationMotiveDTO create(ReservationMotiveDTO reservationMotiveDto) {

        // Vérifier si le motif de réservation existe déjà
        Optional<ReservationMotive> existingMotive = reservationMotiveRepository.findByMotive(reservationMotiveDto.getMotive());

        if (existingMotive.isPresent()) {
            // Lancer une exception si le motif de réservation existe déjà
            throw new DuplicateException("Reservation motive already exists");
        }

        // Mappe le DTO en entité ReservationMotive
        ReservationMotive reservationMotive = ReservationMotiveMapper.mapToReservationMotiveEntity(reservationMotiveDto);

        try {
            // Sauvegarde l'entité ReservationMotive dans le repository
            ReservationMotive savedMotive = reservationMotiveRepository.save(reservationMotive);
            // Mappe l'entité ReservationMotive sauvegardée en DTO et la retourne
            return ReservationMotiveMapper.mapToReservationMotiveDTO(savedMotive);

        } catch (DataIntegrityViolationException e) {
            // Capturer une exception de violation d'intégrité des données
            throw new CreationException("Error occurred while creating reservation motive");
        }
    }


    @Override
    public List<ReservationMotiveDTO> getAll() {

        // Récupère toutes les entités ReservationMotive du repository
        List<ReservationMotive> motives = reservationMotiveRepository.findAll();

        // Vérifie si la liste est vide
        if (motives.isEmpty()) {
            // Lance une ResourceNotFoundException si aucune entité n'est trouvée
            throw new NoContentException("No reservation motives found");
        }

        try {
            // Mappe les entités ReservationMotive en DTO et retourne la liste
            return motives.stream()
                    .map(ReservationMotiveMapper::mapToReservationMotiveDTO)
                    .toList();

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while getting all reservation motives");
        }
    }


    @Override
    public Optional<ReservationMotiveDTO> getById(Integer id) {

        // Cherche une entité ReservationMotive par son identifiant
        ReservationMotive findMotive = reservationMotiveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation motive not found with ID: " + id));

        try {
            // Convertit l'entité ReservationMotive en ReservationMotiveDTO par le ReservationMotiveMapper
            ReservationMotiveDTO reservationMotiveDTO = ReservationMotiveMapper.mapToReservationMotiveDTO(findMotive);

            // Retourne le ReservationMotiveDTO encapsulé dans un Optional
            return Optional.of(reservationMotiveDTO);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while getting reservation motive by ID");
        }
    }


    @Override
    public Optional<ReservationMotiveDTO> update(Integer id, ReservationMotiveDTO reservationMotiveDto) {

        // Cherche l'entité ReservationMotive à mettre à jour par son identifiant
        ReservationMotive motiveToUpdate = reservationMotiveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation motive ID not found: " + id));


        // Vérifier si le nouveau motif existe déjà
        Optional<ReservationMotive> existingMotive = reservationMotiveRepository.findByMotive(reservationMotiveDto.getMotive());

        if (existingMotive.isPresent() && !existingMotive.get().getId().equals(id)) {
            // Lancer une exception si le motif de réservation existe déjà et que ce n'est pas celui en cours de modification
            throw new DuplicateException("Reservation motive already exists");
        }

        try {
            // Met à jour le champ motive de l'entité ReservationMotive avec la valeur du DTO
            motiveToUpdate.setMotive(reservationMotiveDto.getMotive());

            // Sauvegarde l'entité ReservationMotive mise à jour dans le repository
            ReservationMotive updatedMotive = reservationMotiveRepository.save(motiveToUpdate);

            // Mappe l'entité ReservationMotive mise à jour en DTO et la retourne (encapsulée) dans un Optional
            return Optional.of(ReservationMotiveMapper.mapToReservationMotiveDTO(updatedMotive));

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while updating reservation motive");
        }
    }


    @Override
    public void deleteById(Integer id) {
        // Tente de trouver l'entité ReservationMotive par son identifiant
        ReservationMotive motive = reservationMotiveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation motive not found with id: " + id));
        try {
            // Supprime l'entité ReservationMotive trouvée
            reservationMotiveRepository.delete(motive);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("An unexpected error occurred while deleting reservation motive with id: " + id);
        }
    }

}
