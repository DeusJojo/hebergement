package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.*;
import com.afpa.hebergement.model.dto.ReservationDTO;
import com.afpa.hebergement.model.entity.AfpaCenter;
import com.afpa.hebergement.model.entity.Reservation;
import com.afpa.hebergement.model.entity.ReservationMotive;
import com.afpa.hebergement.model.entity.Room;
import com.afpa.hebergement.model.mapper.ReservationMapper;
import com.afpa.hebergement.model.repository.AfpaCenterRepository;
import com.afpa.hebergement.model.repository.ReservationMotiveRepository;
import com.afpa.hebergement.model.repository.ReservationRepository;
import com.afpa.hebergement.model.repository.RoomRepository;
import com.afpa.hebergement.service.entity_service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    public static final String ERROR_DATE = "Error on reservation dates ";
    public static final String BE_EARLIER_THAN_TODAY_S_DATE = "Dates must not be earlier than today's date";
    public static final String RESERVATION_ALREADY_EXIST_FOR_THE_GIVEN_ROOM_AND_DATES = "Reservation already exist for the given room and dates";
    public static final String RESERVATION_MUST_BE_FOR_AT_LEAST_ONE_DAY = "Reservation must be for at least one day.";
    public static final String ROOM_ID_NOT_FOUND = "Room ID not found: ";

    private final ReservationRepository reservationRepository;
    private final ReservationMotiveRepository reservationMotiveRepository;
    private final RoomRepository roomRepository;
    private final AfpaCenterRepository afpaCenterRepository;


    @Override
    public ReservationDTO create(ReservationDTO reservationDto) {


        // Vérifier que la date de début est antérieure à la date de fin
        if (reservationDto.getStartDate().isAfter(reservationDto.getEndDate())) {
            throw new IllegalArgumentException(ERROR_DATE);
        }

        // Vérifier que la date de début et la date de fin ne sont pas dans le passé
        if (reservationDto.getStartDate().isBefore(LocalDate.now()) || reservationDto.getEndDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(BE_EARLIER_THAN_TODAY_S_DATE);
        }

        // Vérifier la durée minimale de réservation (par exemple, au moins 1 jour)
        if (reservationDto.getStartDate().isEqual(reservationDto.getEndDate())) {
            throw new IllegalArgumentException(RESERVATION_MUST_BE_FOR_AT_LEAST_ONE_DAY);
        }

        //mettre la date de réservation à la date du jour
        reservationDto.setReservationDate(LocalDate.now());

        //recherche de l'id de la chambre dans le repository
        Room idRoom = roomRepository.findById(reservationDto.getIdRoom().getId())
                .orElseThrow(() -> new ResourceNotFoundException(ROOM_ID_NOT_FOUND + reservationDto.getIdRoom().getId()));

        // Recherche de l'ID du motif de réservation dans le repository
        ReservationMotive reservationMotive = reservationMotiveRepository.findById(reservationDto.getIdReservationMotive().getId()).orElse(null);
        if (reservationMotive == null) {
            throw new ResourceNotFoundException("Reservation motive ID not found: " + reservationDto.getIdReservationMotive().getId());
        }

        // Vérifier les chevauchements de dates avec les réservations existantes
        List<Reservation> overlappingReservations = reservationRepository.findOverlappingReservations(
                idRoom.getId(),
                reservationDto.getStartDate(),
                reservationDto.getEndDate()
        );

        if (!overlappingReservations.isEmpty()) {
            // Lancer une exception si des chevauchements sont trouvés
            throw new DuplicateException(RESERVATION_ALREADY_EXIST_FOR_THE_GIVEN_ROOM_AND_DATES);
        }


        // Mappe le DTO en entité Reservation
        Reservation reservation = ReservationMapper.mapToReservationEntity(reservationDto);

        try {
            // Sauvegarde l'entité Reservation dans le repository
            Reservation savedReservation = reservationRepository.save(reservation);

            // Si la chambre n'est pas encore réservée, marquer comme réservée
            if (Boolean.FALSE.equals(idRoom.getIsReserved())) {
                idRoom.setIsReserved(true);
                roomRepository.save(idRoom);  // Sauvegarder l'état mis à jour de la chambre
            }

            // Mappe l'entité Reservation sauvegardée en DTO et la retourne
            return ReservationMapper.mapToReservationDTO(savedReservation);

        } catch (DataIntegrityViolationException e) {
            // Capturer une exception de violation d'intégrité des données
            throw new CreationException("Error occurred while creating reservation");
        }
    }


    @Override
    public ReservationDTO create(ReservationDTO reservationDto, Integer roomId, Integer idReservationMotive) {


        // Vérifier que la date de début est antérieure à la date de fin
        if (reservationDto.getStartDate().isAfter(reservationDto.getEndDate())) {
            throw new IllegalArgumentException(ERROR_DATE);
        }

        // Vérifier que la date de début et la date de fin ne sont pas dans le passé
        if (reservationDto.getStartDate().isBefore(LocalDate.now()) || reservationDto.getEndDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(BE_EARLIER_THAN_TODAY_S_DATE);
        }

        // Vérifier la durée minimale de réservation (par exemple, au moins 1 jour)
        if (reservationDto.getStartDate().isEqual(reservationDto.getEndDate())) {
            throw new IllegalArgumentException(RESERVATION_MUST_BE_FOR_AT_LEAST_ONE_DAY);
        }

        //mettre la date de réservation à la date du jour
        reservationDto.setReservationDate(LocalDate.now());

        //recherche de l'id de la chambre dans le repository
        Room idRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException(ROOM_ID_NOT_FOUND + roomId));

        // Recherche de l'ID du motif de réservation dans le repository
        ReservationMotive reservationMotive = reservationMotiveRepository.findById(idReservationMotive)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation motive ID not found: " + idReservationMotive));


        // Vérifier les chevauchements de dates avec les réservations existantes
        List<Reservation> overlappingReservations = reservationRepository.findOverlappingReservations(
                roomId,
                reservationDto.getStartDate(),
                reservationDto.getEndDate()
        );

        if (!overlappingReservations.isEmpty()) {
            // Lancer une exception si des chevauchements sont trouvés
            throw new DuplicateException(RESERVATION_ALREADY_EXIST_FOR_THE_GIVEN_ROOM_AND_DATES);
        }


        // Mappe le DTO en entité Reservation
        Reservation reservation = ReservationMapper.mapToReservationEntity(reservationDto);

        //attribuer les id aux entités
        reservation.setIdRoom(idRoom);
        reservation.setIdReservationMotive(reservationMotive);

        try {
            // Sauvegarde l'entité Reservation dans le repository
            Reservation savedReservation = reservationRepository.save(reservation);

            // Si la chambre n'est pas encore réservée, marquer comme réservée
            if (Boolean.FALSE.equals(idRoom.getIsReserved())) {
                idRoom.setIsReserved(true);
                roomRepository.save(idRoom);  // Sauvegarder l'état mis à jour de la chambre
            }

            // Mappe l'entité Reservation sauvegardée en DTO et la retourne
            return ReservationMapper.mapToReservationDTO(savedReservation);

        } catch (DataIntegrityViolationException e) {
            // Capturer une exception de violation d'intégrité des données
            throw new CreationException("Error occurred while creating reservation");
        }
    }


    public List<ReservationDTO> getReservationsByAfpaCenter(Integer idAfpaCenter) {

        //recherche l'id du centre afpa dans le repository
        AfpaCenter afpaCenter = afpaCenterRepository.findById(idAfpaCenter)
                .orElseThrow(() -> new ResourceNotFoundException("Afpa Center ID not found: " + idAfpaCenter));

        //récupération des reservations d'un centre afpa
        List<Reservation> reservationsList = reservationRepository.findByIdRoom_IdFloor_IdAfpaCenter_Id(afpaCenter.getId());

        // Vérifie si la liste est vide
        if (reservationsList.isEmpty()) {
            // Lance une ResourceNotFoundException si aucune entité n'est trouvée
            throw new NoContentException("No reservations found");
        }

        try {
            // Mappe les entités Reservation en DTO et retourne la liste
            return reservationsList.stream()
                    .map(ReservationMapper::mapToReservationDTO)
                    .toList();

        } catch (Exception e) {
            throw new InternalServerException("Error occurred while getting reservations by Afpa Center ID");
        }
    }


    @Override
    public Optional<ReservationDTO> getById(Integer id) {

        // Cherche une entité Reservation par son identifiant
        Reservation findReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with ID: " + id));

        try {
            // Convertit l'entité Reservation en ReservationDTO par le ReservationMapper
            ReservationDTO reservationDTO = ReservationMapper.mapToReservationDTO(findReservation);

            // Retourne le ReservationDTO encapsulé dans un Optional
            return Optional.of(reservationDTO);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred to recover reservation with ID: " + id);
        }
    }


    @Override
    public Optional<ReservationDTO> update(Integer id, ReservationDTO reservationDto) {

        // Cherche l'entité Reservation à mettre à jour par son identifiant
        Reservation reservationToUpdate = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation ID not found: " + id));

        // Vérifier que la date de début est antérieure à la date de fin
        if (reservationDto.getStartDate().isAfter(reservationDto.getEndDate())) {
            throw new IllegalArgumentException(ERROR_DATE);
        }

        // Vérifier que la date de début et la date de fin ne sont pas dans le passé
        if (reservationDto.getStartDate().isBefore(LocalDate.now()) || reservationDto.getEndDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(BE_EARLIER_THAN_TODAY_S_DATE);
        }

        // Vérifier la durée minimale de réservation (par exemple, au moins 1 jour)
        if (reservationDto.getStartDate().isEqual(reservationDto.getEndDate())) {
            throw new IllegalArgumentException(RESERVATION_MUST_BE_FOR_AT_LEAST_ONE_DAY);
        }

        // Met à jour les champs date d'entrée et date de fin de l'entité Reservation avec les valeurs du DTO
        reservationToUpdate.setStartDate(reservationDto.getStartDate());
        reservationToUpdate.setEndDate(reservationDto.getEndDate());

        // Recherche de l'id de la chambre dans le repository
        Room existingRoom = roomRepository.findById(reservationDto.getIdRoom().getId())
                .orElseThrow(() -> new ResourceNotFoundException(ROOM_ID_NOT_FOUND + reservationDto.getIdRoom().getId()));

        // Vérifier les chevauchements de dates avec les réservations existantes en excluant la réservation actuelle
        List<Reservation> overlappingReservations = reservationRepository.findOverlappingReservationsExcludingCurrent(
                existingRoom.getId(),
                reservationDto.getStartDate(),
                reservationDto.getEndDate(),
                id
        );

        if (!overlappingReservations.isEmpty()) {
            // Lancer une exception si des chevauchements sont trouvés
            throw new DuplicateException(RESERVATION_ALREADY_EXIST_FOR_THE_GIVEN_ROOM_AND_DATES);
        }

        // Mise à jour de la chambre et du motif de réservation
        reservationToUpdate.setIdRoom(existingRoom);

        ReservationMotive reservationMotive = reservationMotiveRepository.findById(reservationDto.getIdReservationMotive().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Reservation motive ID not found"));

        reservationToUpdate.setIdReservationMotive(reservationMotive);

        try {
            // Sauvegarde l'entité Reservation mise à jour dans le repository
            Reservation updatedReservation = reservationRepository.save(reservationToUpdate);

            // Rechercher si la chambre qui a été remplacée est toujours présente dans la table de reservation
            List<Reservation> roomToReplace = reservationRepository.findByIdRoom_Id(reservationToUpdate.getIdRoom().getId());

            // Si elle n'est pas présente
            if (roomToReplace.isEmpty()) {
                // Mettre le statut de la chambre isReserved en false
                reservationToUpdate.getIdRoom().setIsReserved(false);
            }

            // Si la nouvelle chambre mise à jour n'est pas encore réservée, marquer comme réservée
            if (Boolean.FALSE.equals(existingRoom.getIsReserved())) {
                existingRoom.setIsReserved(true);
                roomRepository.save(existingRoom);  // Sauvegarder l'état mis à jour de la chambre
            }

            // Mappe l'entité Reservation mise à jour en DTO et la retourne (encapsulée) dans un Optional
            return Optional.of(ReservationMapper.mapToReservationDTO(updatedReservation));

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while updating the reservation with ID: " + id + e.getMessage());
        }
    }


    @Override
    public void deleteById(Integer id) {

        // Recherche de l'entité Reservation par son identifiant
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));

        try {

            // Supprime l'entité Reservation trouvée
            reservationRepository.delete(reservation);

            // Rechercher si la chambre est toujours présente dans la table de reservation sur d'autres dates
            List<Reservation> roomReserved = reservationRepository.findByIdRoom_Id(reservation.getIdRoom().getId());

            // si elle n'est pas présente
            if (roomReserved.isEmpty()) {

                // Rechercher la chambre en utilisant l'ID de la chambre
                Room room = roomRepository.findById(reservation.getIdRoom().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + reservation.getIdRoom().getId()));

                //mettre le statut de la chambre isReserved en false
                room.setIsReserved(false);

                // Sauvegarder les modifications apportées à la chambre
                roomRepository.save(room);
            }

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("An unexpected error occurred while deleting the reservation with id: " + id);
        }

    }


}

