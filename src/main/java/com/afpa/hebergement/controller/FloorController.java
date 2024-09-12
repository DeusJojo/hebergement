package com.afpa.hebergement.controller;


import com.afpa.hebergement.model.dto.FloorDTO;
import com.afpa.hebergement.service.entity_service.FloorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Floor", description = "Endpoints to manage Reservation floors")
@RestController
@RequestMapping("/api/floors")
@AllArgsConstructor
@Validated
public class FloorController {

    private final FloorService floorService;

    // Endpoint pour créer un nouvel étage
    @PostMapping("/create")
    @Operation(summary = "Create new floor")
    public ResponseEntity<FloorDTO> createFloor(@Valid @RequestBody FloorDTO floorDto) {
        // Appel du service pour créer un nouvel étage à partir du DTO
        FloorDTO createdFloor = floorService.create(floorDto);
        // Retourne l'étage créé avec un statut HTTP 201 (Created)
        return new ResponseEntity<>(createdFloor, HttpStatus.CREATED);
    }


    // Endpoint pour récupérer les étages par centre Afpa via les chambres
    @GetMapping("/afpa-center/{afpaCenterId}")
    @Operation(summary = "Get Floors by Afpa Center via Rooms")
    public ResponseEntity<List<FloorDTO>> getFloorsByAfpaCenter(@PathVariable("afpaCenterId") Integer afpaCenterId) {
        // Appel du service pour obtenir les étages par centre Afpa via les chambres
        List<FloorDTO> floors = floorService.getFloorsByAfpaCenter(afpaCenterId);
        // Retourne les étages avec un statut HTTP 200 (OK)
        return ResponseEntity.ok(floors);
    }


    // Endpoint pour récupérer un étage par son ID
    @GetMapping("/{id}")
    @Operation(summary = "Get floor by ID")
    public ResponseEntity<FloorDTO> getFloorById(@PathVariable("id") Integer idFloor) {
        // Appel du service pour obtenir un étage par son ID
        Optional<FloorDTO> floor = floorService.getById(idFloor);
        // Vérifie si l'étage est présent et retourne l'étage avec un statut HTTP 200 (OK)
        return floor.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    // Endpoint pour mettre à jour un étage par son ID
    @PutMapping("/update/{id}")
    @Operation(summary = "Update floor by ID")
    public ResponseEntity<FloorDTO> updateFloor(@PathVariable("id") Integer idFloor, @Valid @RequestBody FloorDTO floorDto) {
        // Appel du service pour mettre à jour l'étage existant avec les nouvelles données
        Optional<FloorDTO> updatedFloor = floorService.update(idFloor, floorDto);
        // Vérifie si l'étage mis à jour est présent et retourne l'étage mis à jour avec un statut HTTP 200 (OK)
        return updatedFloor.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Endpoint pour supprimer un étage par son ID
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete floor by ID")
    public ResponseEntity<HttpStatus> deleteFloor(@PathVariable("id") Integer idFloor) {
        // Appel du service pour supprimer l'étage par son ID
        floorService.deleteById(idFloor);
        // Retourne un statut HTTP 204 (No Content) après suppression
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
