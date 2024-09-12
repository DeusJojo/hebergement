package com.afpa.hebergement.controller;


import com.afpa.hebergement.model.dto.RoomDTO;
import com.afpa.hebergement.service.entity_service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Room", description = "Endpoints to manage rooms")
@RestController
@RequestMapping("/api/rooms")
@AllArgsConstructor
public class RoomController {


    private final RoomService roomService;


    // Endpoint pour créer une nouvelle chambre
    @PostMapping("/create")
    @Operation(summary = "Create new room")
    public ResponseEntity<RoomDTO> createRoom(@Valid @RequestBody RoomDTO roomDto) {
        // Appel du service pour créer une nouvelle chambre à partir du DTO
        RoomDTO createdRoom = roomService.create(roomDto);
        // Retourne la chambre créée avec un statut HTTP 201 (Created)
        return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
    }


    // Endpoint pour récupérer toutes les chambres par centre Afpa
    @GetMapping("/afpa-center/{idAfpaCenter}")
    @Operation(summary = "Get all rooms by Afpa Center")
    public ResponseEntity<List<RoomDTO>> getAllByAfpaCenter(@PathVariable("idAfpaCenter") Integer idAfpaCenter) {
        List<RoomDTO> rooms = roomService.getAllByAfpaCenter(idAfpaCenter);
        return ResponseEntity.ok(rooms);
    }


    // Endpoint pour récupérer les chambres par centre Afpa avec pagination
    @GetMapping("/afpa-center/{idAfpaCenter}/page")
    @Operation(summary = "Get rooms by Afpa Center with pagination")
    public ResponseEntity<Page<RoomDTO>> getRoomsByAfpaCenterPage(
            @PathVariable("idAfpaCenter") Integer idAfpaCenter,
            // Définit un paramètre de requête "page" avec une valeur par défaut de 0 (1ère page)
            @RequestParam(defaultValue = "0") int page,
            // Définit un paramètre de requête "size" avec une valeur par défaut de 5 (5 éléments par page)
            @RequestParam(defaultValue = "5") int size) {

        // Appel du service pour obtenir les utilisateurs par centre Afpa avec pagination
        Page<RoomDTO> roomsPage = roomService.getAllByAfpaCenter(idAfpaCenter, page, size);

        // Retourne la page des chambres avec un statut HTTP 200 (OK)
        return ResponseEntity.ok(roomsPage);
    }


    // Endpoint pour récupérer une chambre par son ID
    @GetMapping("/{id}")
    @Operation(summary = "Get room by ID")
    public ResponseEntity<RoomDTO> getRoomById(@PathVariable("id") Integer idRoom) {
        // Appel du service pour obtenir une chambre par son ID
        Optional<RoomDTO> room = roomService.getById(idRoom);

        // Retourne la chambre avec un statut HTTP 200 (OK) si présente
        return room.map(ResponseEntity::ok)
                // Retourne une réponse avec un statut HTTP 404 (Not Found) si la chambre n'est pas trouvée
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Endpoint pour récupérer les chambres réservées par centre Afpa
    @GetMapping("/reserved/afpa-center/{idAfpaCenter}")
    @Operation(summary = "Get reserved rooms by Afpa Center")
    public ResponseEntity<List<RoomDTO>> getRoomByReservationAndAfpaCenter(@PathVariable("idAfpaCenter") Integer idAfpaCenter) {
        List<RoomDTO> rooms = roomService.getByReservedRoomAndIdAfpaCenter(idAfpaCenter);
        return ResponseEntity.ok(rooms);
    }


    // Endpoint pour récupérer les chambres libres par centre Afpa
    @GetMapping("/available/afpa-center/{idAfpaCenter}")
    @Operation(summary = "Get available rooms by Afpa Center")
    public ResponseEntity<List<RoomDTO>> getRoomByAvailabilityAndAfpaCenter(@PathVariable("idAfpaCenter") Integer idAfpaCenter) {
        List<RoomDTO> rooms = roomService.getByAvailableRoomAndIdAfpaCenter(idAfpaCenter);
        return ResponseEntity.ok(rooms);
    }

    // Endpoint pour récupérer les chambres occupées par centre Afpa
    @GetMapping("/occupied/afpa-center/{idAfpaCenter}")
    @Operation(summary = "Get occupied rooms by Afpa Center")
    public ResponseEntity<List<RoomDTO>> getByOccupiedRoomAndAfpaCenter(@PathVariable("idAfpaCenter") Integer idAfpaCenter) {
        List<RoomDTO> rooms = roomService.getByOccupiedRoomAndIdAfpaCenter(idAfpaCenter);
        return ResponseEntity.ok(rooms);
    }


    // Endpoint pour récupérer les chambres réservées aux femmes par centre Afpa
    @GetMapping("/women/afpa-center/{idAfpaCenter}")
    @Operation(summary = "Get rooms for women by Afpa Center")
    public ResponseEntity<List<RoomDTO>> getByWomanRoomAndAfpaCenter(@PathVariable("idAfpaCenter") Integer idAfpaCenter) {
        List<RoomDTO> rooms = roomService.getByWomanRoomAndIdAfpaCenter(idAfpaCenter);
        return ResponseEntity.ok(rooms);
    }


    // Endpoint pour récupérer une chambre par numéro de chambre et centre Afpa
    @GetMapping("/afpa-center/{idAfpaCenter}/room/{roomNumber}")
    @Operation(summary = "Get a room by room number and Afpa Center ID")
    public ResponseEntity<RoomDTO> getByRoomNumberAndIdAfpaCenter(@PathVariable("idAfpaCenter") Integer idAfpaCenter,
                                                                  @PathVariable("roomNumber") String roomNumber) {
        // Appel du service pour récupérer une chambre par son numéro de chambre
        Optional<RoomDTO> room = roomService.getByRoomNumberAndIdAfpaCenter(roomNumber, idAfpaCenter);

        return room.map(ResponseEntity::ok)
                // Retourne une réponse avec un statut HTTP 404 (Not Found) si la chambre n'est pas trouvée
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Endpoint pour mettre à jour une chambre par son ID
    @PutMapping("/update/{id}")
    @Operation(summary = "Update room by ID")
    public ResponseEntity<RoomDTO> updateRoom(@PathVariable("id") Integer idRoom, @Valid @RequestBody RoomDTO roomDto) {
        // Appel du service pour mettre à jour la chambre existante avec les nouvelles données
        Optional<RoomDTO> updatedRoom = roomService.update(idRoom, roomDto);

        // Retourne la chambre mise à jour avec un statut HTTP 200 (OK) si présente
        return updatedRoom.map(ResponseEntity::ok)
                // Retourne une réponse avec un statut HTTP 404 (Not Found) si la chambre n'est pas trouvée
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Endpoint pour supprimer une chambre par son ID
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete room by ID")
    public ResponseEntity<HttpStatus> deleteRoom(@PathVariable("id") Integer idRoom) {
        // Appel du service pour supprimer la chambre par son ID
        roomService.deleteById(idRoom);
        // Retourne un statut HTTP 204 (No Content) après suppression
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

