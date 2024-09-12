package com.afpa.hebergement.controller;

import com.afpa.hebergement.model.dto.ReservationMotiveDTO;
import com.afpa.hebergement.service.entity_service.ReservationMotiveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Reservation motive", description = "Endpoints to manage Reservation Motive")
@RestController
@RequestMapping("/api/reservation-motives")
@AllArgsConstructor
public class ReservationMotiveController {


    private final ReservationMotiveService reservationMotiveService;


    // Endpoint pour créer un nouveau motif de réservation
    @PostMapping("/create")
    @Operation(summary = "Create new reservation motive")
    public ResponseEntity<ReservationMotiveDTO> createReservationMotive(@Valid @RequestBody ReservationMotiveDTO reservationMotiveDto) {
        // Appel du service pour créer un nouveau motif de réservation à partir du DTO
        ReservationMotiveDTO createdMotive = reservationMotiveService.create(reservationMotiveDto);
        // Retourne le motif de réservation créé avec un statut HTTP 201 (Created)
        return new ResponseEntity<>(createdMotive, HttpStatus.CREATED);
    }

    // Endpoint pour récupérer tous les motifs de réservation
    @GetMapping
    @Operation(summary = "Get all reservation motives")
    public ResponseEntity<List<ReservationMotiveDTO>> getAllReservationMotives() {
        // Appel du service pour obtenir tous les motifs de réservation
        List<ReservationMotiveDTO> motives = reservationMotiveService.getAll();
        // Retourne la liste des motifs de réservation avec un statut HTTP 200 (OK)
        return ResponseEntity.ok(motives);
    }


    // Endpoint pour récupérer un motif de réservation par son ID
    @GetMapping("/{id}")
    @Operation(summary = "Get reservation motive by ID")
    public ResponseEntity<ReservationMotiveDTO> getReservationMotiveById(@PathVariable("id") Integer idMotive) {
        // Appel du service pour obtenir un motif de réservation par son ID
        Optional<ReservationMotiveDTO> motive = reservationMotiveService.getById(idMotive);

        // Retourne le motif de réservation avec un statut HTTP 200 (OK) si présent
        return motive.map(ResponseEntity::ok)
                // Retourne une réponse avec un statut HTTP 404 (Not Found) si le motif de réservation n'est pas trouvé
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Endpoint pour mettre à jour un motif de réservation par son ID
    @PutMapping("/update/{id}")
    @Operation(summary = "Update reservation motive by ID")
    public ResponseEntity<ReservationMotiveDTO> updateReservationMotive(@PathVariable("id") Integer idMotive, @Valid @RequestBody ReservationMotiveDTO reservationMotiveDto) {
        // Appel du service pour mettre à jour le motif de réservation existant avec les nouvelles données
        Optional<ReservationMotiveDTO> updatedMotive = reservationMotiveService.update(idMotive, reservationMotiveDto);

        // Retourne le motif de réservation mis à jour avec un statut HTTP 200 (OK) si présent
        return updatedMotive.map(ResponseEntity::ok)
                // Retourne une réponse avec un statut HTTP 404 (Not Found) si le motif de réservation n'est pas trouvé
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Endpoint pour supprimer un motif de réservation par son ID
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete reservation motive by ID")
    public ResponseEntity<HttpStatus> deleteReservationMotive(@PathVariable("id") Integer idMotive) {
        // Appel du service pour supprimer le motif de réservation par son ID
        reservationMotiveService.deleteById(idMotive);
        // Retourne un statut HTTP 204 (No Content) après suppression
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

