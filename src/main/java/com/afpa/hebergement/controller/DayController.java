package com.afpa.hebergement.controller;


import com.afpa.hebergement.model.dto.DayDTO;
import com.afpa.hebergement.service.entity_service.DayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Tag(name = "Day", description = "Endpoints to manage days")
@RestController
@RequestMapping("/api/days")
@AllArgsConstructor
public class DayController {


    private final DayService dayService;


    // Endpoint pour créer un nouveau jour
    @PostMapping("/create")
    @Operation(summary = "Create new day")
    public ResponseEntity<DayDTO> createDay(@Valid @RequestBody DayDTO dayDto) {
        // Appel du service pour créer un nouveau jour à partir du DTO
        DayDTO createdDay = dayService.create(dayDto);
        // Retourne le jour créé avec un statut HTTP 201 (Created)
        return new ResponseEntity<>(createdDay, HttpStatus.CREATED);
    }


    // Endpoint pour récupérer tous les jours
    @GetMapping
    @Operation(summary = "Get all days")
    public ResponseEntity<List<DayDTO>> getAllDays() {
        // Appel du service pour obtenir tous les jours
        List<DayDTO> days = dayService.getAll();
        // Retourne la liste des jours avec un statut HTTP 200 (OK)
        return ResponseEntity.ok(days);
    }


    // Endpoint pour récupérer un jour par son ID
    @GetMapping("/{id}")
    @Operation(summary = "Get day by ID")
    public ResponseEntity<DayDTO> getDayById(@PathVariable("id") Integer idDay) {
        // Appel du service pour obtenir un jour par son ID
        Optional<DayDTO> day = dayService.getById(idDay);

        // Retourne le jour avec un statut HTTP 200 (OK) s'il est présent
        // Sinon, retourne une réponse avec un statut HTTP 404 (Not Found)
        return day.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Endpoint pour mettre à jour un jour par son ID
    @PutMapping("/update/{id}")
    @Operation(summary = "Update day by ID")
    public ResponseEntity<DayDTO> updateDay(@PathVariable("id") Integer idDay, @Valid @RequestBody DayDTO dayDto) {
        // Appel du service pour mettre à jour le jour existant avec les nouvelles données
        Optional<DayDTO> updatedDay = dayService.update(idDay, dayDto);

        // Retourne le jour mis à jour avec un statut HTTP 200 (OK) s'il est présent
        // Sinon, retourne une réponse avec un statut HTTP 404 (Not Found)
        return updatedDay.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Endpoint pour supprimer un jour par son ID
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete day by ID")
    public ResponseEntity<HttpStatus> deleteDay(@PathVariable("id") Integer idDay) {
        // Appel du service pour supprimer le jour par son ID
        dayService.deleteById(idDay);
        // Retourne un statut HTTP 204 (No Content) après suppression
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
