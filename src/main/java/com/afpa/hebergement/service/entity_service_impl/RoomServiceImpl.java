package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.*;
import com.afpa.hebergement.model.dto.RoomDTO;
import com.afpa.hebergement.model.entity.*;
import com.afpa.hebergement.model.mapper.RoomMapper;
import com.afpa.hebergement.model.repository.AfpaCenterRepository;
import com.afpa.hebergement.model.repository.FloorRepository;
import com.afpa.hebergement.model.repository.RoomRepository;
import com.afpa.hebergement.service.entity_service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {


    public static final String AFPA_CENTER_ID_NOT_FOUND = "Afpa Center ID not found: ";
    private final RoomRepository roomRepository;
    private final FloorRepository floorRepository;
    private final AfpaCenterRepository afpaCenterRepository;


    @Override
    public RoomDTO create(RoomDTO roomDto) {


        // Vérifier si l'ID de l'étage existe
        Floor floor = floorRepository.findById(roomDto.getIdFloor().getId()).orElse(null);
        if (floor == null) {
            throw new ResourceNotFoundException("Floor ID not found: " + roomDto.getIdFloor().getId());
        }

        //recherche du centre afpa par son id
        AfpaCenter afpaCenter = afpaCenterRepository.findById(roomDto.getIdFloor().getIdAfpaCenter().getId()).orElse(null);
        if (afpaCenter == null) {
            throw new ResourceNotFoundException("Afpa center ID not found: " + roomDto.getIdFloor().getIdAfpaCenter());
        }

        // Vérifier si la chambre existe déjà par numéro de chambre et ID de l'étage
        Optional<Room> existingRoom = roomRepository.findByRoomNumberAndIdFloor_Id(
                roomDto.getRoomNumber(),
                floor.getId()
        );


        if (existingRoom.isPresent()) {
            // Lancer une exception si la chambre existe déjà
            throw new DuplicateException("Room already exists with room number: " + roomDto.getRoomNumber() + "for " + afpaCenter.getCenterName());
        }

        try {

            // Mappe le DTO en entité Room
            Room room = RoomMapper.mapToRoomEntity(roomDto);

            // Sauvegarde l'entité Room dans le repository
            Room savedRoom = roomRepository.save(room);

            // Mappe l'entité Room sauvegardée en DTO et la retourne
            return RoomMapper.mapToRoomDTO(savedRoom);

        } catch (DataIntegrityViolationException e) {
            // Capturer une exception de violation d'intégrité des données
            throw new CreationException("Error occurred while creating room");
        }
    }


    public List<RoomDTO> getAllByAfpaCenter(Integer idAfpaCenter) {

        //recherche de l'id du centre afpa dans le repository
        AfpaCenter afpacenter = afpaCenterRepository.findById(idAfpaCenter)
                .orElseThrow(() -> new ResourceNotFoundException(AFPA_CENTER_ID_NOT_FOUND + idAfpaCenter));

        // Cherche les entités Room par id afpa center
        List<Room> roomsList = roomRepository.findByIdFloor_IdAfpaCenter_Id(afpacenter.getId());

        // Vérifie si la liste est vide
        if (roomsList.isEmpty()) {
            // Lance une ResourceNotFoundException si aucune entité n'est trouvée
            throw new NoContentException("No Room(s) found");
        }

        try {
            // Mappe les entités Room en DTO et retourne la liste
            return roomsList.stream()
                    .map(RoomMapper::mapToRoomDTO)
                    .toList();

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while getting Room(s) by ID");
        }

    }


    @Override
    public Page<RoomDTO> getAllByAfpaCenter(Integer idAfpaCenter, Integer page, Integer size) {

        // Recherche de l'id du centre Afpa dans le repository
        AfpaCenter afpaCenter = afpaCenterRepository.findById(idAfpaCenter)
                .orElseThrow(() -> new ResourceNotFoundException("Afpa Center ID not found: " + idAfpaCenter));

        // Crée un objet Pageable avec le numéro de page, la taille de la page, et le tri par ID croissant
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        // Cherche les entités Room par id Afpa center avec pagination
        Page<Room> roomsPage = roomRepository.findByIdFloor_IdAfpaCenter_Id(afpaCenter.getId(), pageable);

        // Vérifie si la liste est vide
        if (roomsPage.isEmpty()) {
            // Lance une NoContentException si aucune entité n'est trouvée
            throw new NoContentException("No room(s) found");
        }

        try {
            // Mappe les entités Room en DTO et retourne la page
            return roomsPage.map(RoomMapper::mapToRoomDTO);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while getting room(s) by pagination");
        }
    }


    @Override
    public Optional<RoomDTO> getById(Integer id) {

        // Cherche une entité Room par son identifiant
        Room findRoom = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + id));

        try {
            // Convertit l'entité Room en RoomDTO par le RoomMapper
            RoomDTO roomDTO = RoomMapper.mapToRoomDTO(findRoom);

            // Retourne le RoomDTO encapsulé dans un Optional
            return Optional.of(roomDTO);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred to recover room with ID: " + id);
        }
    }


    public List<RoomDTO> getByReservedRoomAndIdAfpaCenter(Integer idAfpaCenter) {

        // Recherche de l'ID du centre Afpa dans le repository
        AfpaCenter afpaCenter = afpaCenterRepository.findById(idAfpaCenter)
                .orElseThrow(() -> new ResourceNotFoundException(AFPA_CENTER_ID_NOT_FOUND + idAfpaCenter));

        // Cherche les entités Room réservées par ID Afpa Center
        List<Room> roomsList = roomRepository.findByIsReservedTrueAndIdFloor_IdAfpaCenter_Id(afpaCenter.getId());

        // Vérifie si la liste est vide
        if (roomsList.isEmpty()) {
            // Lance une NoContentException si aucune entité n'est trouvée
            throw new NoContentException("No reserved rooms found");
        }

        try {
            // Mappe les entités Room en DTO et retourne la liste
            return roomsList.stream()
                    .map(RoomMapper::mapToRoomDTO)
                    .toList();

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while getting reserved rooms by Afpa Center ID");
        }
    }


    public List<RoomDTO> getByAvailableRoomAndIdAfpaCenter(Integer idAfpaCenter) {

        // Recherche de l'ID du centre Afpa dans le repository
        AfpaCenter afpaCenter = afpaCenterRepository.findById(idAfpaCenter)
                .orElseThrow(() -> new ResourceNotFoundException(AFPA_CENTER_ID_NOT_FOUND + idAfpaCenter));

        // Cherche les entités Room utilisables par ID Afpa Center
        List<Room> roomsList = roomRepository.findByIsUsableTrueAndIdFloor_IdAfpaCenter_Id(afpaCenter.getId());

        // Vérifie si la liste est vide
        if (roomsList.isEmpty()) {
            // Lance une NoContentException si aucune entité n'est trouvée
            throw new NoContentException("No usable rooms found");
        }

        try {
            // Mappe les entités Room en DTO et retourne la liste
            return roomsList.stream()
                    .map(RoomMapper::mapToRoomDTO)
                    .toList();

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while getting usable rooms by Afpa Center ID");
        }
    }


    public List<RoomDTO> getByOccupiedRoomAndIdAfpaCenter(Integer idAfpaCenter) {

        // Recherche de l'ID du centre Afpa dans le repository
        AfpaCenter afpaCenter = afpaCenterRepository.findById(idAfpaCenter)
                .orElseThrow(() -> new ResourceNotFoundException(AFPA_CENTER_ID_NOT_FOUND + idAfpaCenter));

        // Cherche les entités Room non utilisables par ID Afpa Center
        List<Room> roomsList = roomRepository. findByIsUsableFalseAndIdFloor_IdAfpaCenter_Id(afpaCenter.getId());

        // Vérifie si la liste est vide
        if (roomsList.isEmpty()) {
            // Lance une NoContentException si aucune entité n'est trouvée
            throw new NoContentException("No occupied rooms found");
        }

        try {
            // Mappe les entités Room en DTO et retourne la liste
            return roomsList.stream()
                    .map(RoomMapper::mapToRoomDTO)
                    .toList();

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while getting occupied rooms by Afpa Center ID");
        }
    }


    public List<RoomDTO> getByWomanRoomAndIdAfpaCenter(Integer idAfpaCenter) {

        // Recherche de l'ID du centre Afpa dans le repository
        AfpaCenter afpaCenter = afpaCenterRepository.findById(idAfpaCenter)
                .orElseThrow(() -> new ResourceNotFoundException(AFPA_CENTER_ID_NOT_FOUND + idAfpaCenter));

        // Cherche les entités Room réservées aux femmes par ID Afpa Center
        List<Room> roomsList = roomRepository.findByIdFloor_IsForWomenTrueAndIdFloor_IdAfpaCenter_Id(afpaCenter.getId());

        // Vérifie si la liste est vide
        if (roomsList.isEmpty()) {
            // Lance une NoContentException si aucune entité n'est trouvée
            throw new NoContentException("No rooms for women found");
        }

        try {
            // Mappe les entités Room en DTO et retourne la liste
            return roomsList.stream()
                    .map(RoomMapper::mapToRoomDTO)
                    .toList();

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while getting rooms for women by Afpa Center ID");
        }
    }


    public Optional<RoomDTO> getByRoomNumberAndIdAfpaCenter(String roomNumber, Integer idAfpaCenter) {

        // Recherche de l'ID du centre Afpa dans le repository
        AfpaCenter afpaCenter = afpaCenterRepository.findById(idAfpaCenter)
                .orElseThrow(() -> new ResourceNotFoundException(AFPA_CENTER_ID_NOT_FOUND + idAfpaCenter));

        // Cherche la chambre par son numéro de chambre et par ID Afpa Center
        Room room = roomRepository.findByRoomNumberAndIdFloor_IdAfpaCenter_Id(roomNumber, afpaCenter.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found "));

        try {
            // Mappe l'entité Room en DTO et retourne l'entité chambre trouvée par son numéro de chambre
            return Optional.of(RoomMapper.mapToRoomDTO(room));

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while getting Room by Room Number") ;
        }

    }


    @Override
    public Optional<RoomDTO> update(Integer id, RoomDTO roomDto) {

        // Cherche l'entité Room à mettre à jour par son identifiant
        Room roomToUpdate = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room ID not found: " + id));

        // Met à jour les champs de l'entité Room avec les valeurs du DTO
        roomToUpdate.setRoomNumber(roomDto.getRoomNumber());
        roomToUpdate.setRoomKeyNumber(roomDto.getRoomKeyNumber());
        roomToUpdate.setIsUsable(roomDto.getIsUsable());
        roomToUpdate.setBadgeNumber(roomDto.getBadgeNumber());
        roomToUpdate.setFridgeKeyNumber(roomDto.getFridgeKeyNumber());
        roomToUpdate.setIsReserved(roomDto.getIsReserved());


        // Vérifie si l'ID de Floor n'est pas null et met à jour
        if (roomDto.getIdFloor() != null) {
            Floor floor = floorRepository.findById(roomDto.getIdFloor().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Floor ID not found"));
            roomToUpdate.setIdFloor(floor);
        } else {
            throw new IllegalArgumentException("idFloor cannot be null");
        }

        try {
            // Sauvegarde l'entité Room mise à jour dans les
            Room updatedRoom = roomRepository.save(roomToUpdate);

            // Mappe l'entité Room mise à jour en DTO et la retourne (encapsulée) dans un Optional
            return Optional.of(RoomMapper.mapToRoomDTO(updatedRoom));

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while updating the room with ID: " + id);
        }

    }


    @Override
    public void deleteById(Integer id) {

        // Tente de trouver l'entité Room par son identifiant
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));

        try {
            // Supprime l'entité Room trouvée
            roomRepository.deleteById(room.getId());

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("An unexpected error occurred while deleting the room with id: " + id);
        }
    }

}
