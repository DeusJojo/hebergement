package com.afpa.hebergement.controller;

import com.afpa.hebergement.model.dto.ReservationDTO;
import com.afpa.hebergement.service.entity_service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Reservation", description = "Endpoints to manage reservations")
@RestController
@RequestMapping("/api/reservations")
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;


    // Endpoint pour créer une nouvelle réservation
    @PostMapping("/create")
    @Operation(summary = "Create new reservation")
    public ResponseEntity<ReservationDTO> createReservation(@Valid @RequestBody ReservationDTO reservationDto) {
        // Appel du service pour créer une nouvelle réservation à partir du DTO
        ReservationDTO createdReservation = reservationService.create(reservationDto);
        // Retourne la réservation créée avec un statut HTTP 201 (Created)
        return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
    }

    // Endpoint pour créer une nouvelle réservation
    @PostMapping("/create/{idRoom}/{idReservationMotive}")
    @Operation(summary = "Create new reservation by room and reservation motive")
    public ResponseEntity<ReservationDTO> createReservation(@PathVariable("idRoom") Integer roomId,
                                                            @PathVariable("idReservationMotive") Integer reservationMotiveId,
                                                            @Valid @RequestBody ReservationDTO reservationDto) {
        // Appel du service pour créer une nouvelle réservation à partir du DTO, de l'ID de la chambre et de l'ID du motif de réservation
        ReservationDTO createdReservation = reservationService.create(reservationDto, roomId, reservationMotiveId);
        // Retourne la réservation créée avec un statut HTTP 201 (Created)
        return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
    }


    // Endpoint pour récupérer les réservations par centre Afpa
    @GetMapping("/afpa-center/{idAfpaCenter}")
    @Operation(summary = "Get reservations by Afpa Center")
    public ResponseEntity<List<ReservationDTO>> getReservationsByAfpaCenter(@PathVariable("idAfpaCenter") Integer idAfpaCenter) {
        // Appel du service pour obtenir toutes les réservations par centre afpa
        List<ReservationDTO> reservations = reservationService.getReservationsByAfpaCenter(idAfpaCenter);
        // Retourne la liste des réservations avec un statut HTTP 200 (OK)
        return ResponseEntity.ok(reservations);
    }

    // Endpoint pour récupérer une réservation par son ID
    @GetMapping("/{id}")
    @Operation(summary = "Get reservation by ID")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable("id") Integer idReservation) {
        // Appel du service pour obtenir une réservation par son ID
        Optional<ReservationDTO> reservation = reservationService.getById(idReservation);

        // Retourne la réservation avec un statut HTTP 200 (OK) si elle est présente
        return reservation.map(ResponseEntity::ok)
                // Retourne une réponse avec un statut HTTP 404 (Not Found) si la reservation n'est pas trouvée
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Endpoint pour mettre à jour une réservation par son ID
    @PutMapping("/update/{id}")
    @Operation(summary = "Update reservation by ID")
    public ResponseEntity<ReservationDTO> updateReservation(@PathVariable("id") Integer idReservation, @Valid @RequestBody ReservationDTO reservationDto) {
        // Appel du service pour mettre à jour la réservation existante avec les nouvelles données
        Optional<ReservationDTO> updatedReservation = reservationService.update(idReservation, reservationDto);

        // Retourne la réservation mise à jour avec un statut HTTP 200 (OK) si elle est présente
        return updatedReservation.map(ResponseEntity::ok)
                // Retourne une réponse avec un statut HTTP 404 (Not Found) si la reservation n'est pas trouvée
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Endpoint pour supprimer une réservation par son ID
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete reservation by ID")
    public ResponseEntity<HttpStatus> deleteReservation(@PathVariable("id") Integer idReservation) {
        // Appel du service pour supprimer la réservation par son ID
        reservationService.deleteById(idReservation);
        // Retourne un statut HTTP 204 (No Content) après suppression
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
