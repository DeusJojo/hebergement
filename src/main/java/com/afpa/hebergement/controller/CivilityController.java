package com.afpa.hebergement.controller;

import com.afpa.hebergement.model.dto.CivilityDTO;
import com.afpa.hebergement.service.entity_service.CivilityService;
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

@Tag(name = "Civility", description = "Endpoints to manage civilities")
@RestController
@RequestMapping("/api/civilities")
@AllArgsConstructor
@Validated
public class CivilityController {


    private final CivilityService civilityService;


    // Endpoint pour créer une nouvelle civilité
    @PostMapping("/create")
    @Operation(summary = "Create new civility")
    public ResponseEntity<CivilityDTO> createCivility(@Valid @RequestBody CivilityDTO civilityDto) {
        // Appel du service pour créer une nouvelle civilité à partir du DTO
        CivilityDTO createdCivility = civilityService.create(civilityDto);
        // Retourne la civilité créée avec un statut HTTP 201 (Created)
        return new ResponseEntity<>(createdCivility, HttpStatus.CREATED);
    }


    // Endpoint pour récupérer toutes les civilités
    @GetMapping
    @Operation(summary = "Get all civilities")
    public ResponseEntity<List<CivilityDTO>> getAllCivilities() {
        // Appel du service pour obtenir toutes les civilités
        List<CivilityDTO> civilities = civilityService.getAll();
        // Retourne la liste des civilités avec un statut HTTP 200 (OK)
        return ResponseEntity.ok(civilities);
    }

    // Endpoint pour récupérer une civilité par son ID
    @GetMapping("/{id}")
    @Operation(summary = "Get civility by ID")
    public ResponseEntity<CivilityDTO> getCivilityById(@PathVariable("id") Integer idCivility) {
        // Appel du service pour obtenir une civilité par son ID
        Optional<CivilityDTO> civility = civilityService.getById(idCivility);

        // Retourne la civilité avec un statut HTTP 200 (OK)
        return civility.map(ResponseEntity::ok)
                // Retourne une réponse avec un statut HTTP 404 (Not Found) si la civilité n'est pas trouvée
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Endpoint pour mettre à jour une civilité par son ID
    @PutMapping("/update/{id}")
    @Operation(summary = "Update civility by ID")
    public ResponseEntity<CivilityDTO> updateCivility(@PathVariable("id") Integer idCivility, @Valid @RequestBody CivilityDTO civilityDto) {
        // Appel du service pour mettre à jour la civilité existante avec les nouvelles données
        Optional<CivilityDTO> updatedCivility = civilityService.update(idCivility, civilityDto);

        // Retourne la civilité mise à jour avec un statut HTTP 200 (OK)
        return updatedCivility.map(ResponseEntity::ok)
                // Retourne une réponse avec un statut HTTP 404 (Not Found) si la civilité n'est pas trouvée
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    // Endpoint pour supprimer une civilité par son ID
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete civility by ID")
    public ResponseEntity<HttpStatus> deleteCivility(@PathVariable("id") Integer idCivility) {
        // Appel du service pour supprimer la civilité par son ID
        civilityService.deleteById(idCivility);
        // Retourne un statut HTTP 204 (No Content) après suppression
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

