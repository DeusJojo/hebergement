package com.afpa.hebergement.controller;


import com.afpa.hebergement.model.dto.IntendantDTO;
import com.afpa.hebergement.service.entity_service.IntendantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// Annotation Swagger pour la documentation des endpoints du contrôleur
@Tag(name = "Intendant", description = "Endpoints to manage intendants")
// Indique que cette classe est un contrôleur REST
@RestController
// Définit le chemin de base pour tous les endpoints de ce contrôleur
@RequestMapping("/api/intendants")
// Génère un constructeur avec un argument pour chaque champ
@AllArgsConstructor
public class IntendantController {


    // Injection du service IntendantService
    private final IntendantService intendantService;



    // Endpoint pour récupérer un intendant par son ID
    @GetMapping("/{id}")
    @Operation(summary = "Get intendant by ID")
    public ResponseEntity<IntendantDTO> getIntendantById(@PathVariable("id") Integer idIntendant) {
        // Appel du service pour obtenir un intendant par son ID
        Optional<IntendantDTO> intendant = intendantService.getById(idIntendant);

        // Retourne l'intendant avec un statut HTTP 200 (OK)
        return intendant.map(ResponseEntity::ok)
                // Retourne une réponse avec un statut HTTP 404 (Not Found) si l'intendant n'est pas trouvé
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    // Endpoint pour récupérer les intendants par centre Afpa
    @GetMapping("/afpa-center/{idAfpaCenter}")
    @Operation(summary = "Get intendants by Afpa Center")
    public ResponseEntity<List<IntendantDTO>> getIntendantsByAfpaCenter(@PathVariable("idAfpaCenter") Integer idAfpaCenter) {
        // Appel du service pour obtenir tous les intendants par centre afpa
        List<IntendantDTO> intendants = intendantService.getAllIntendantsByAfpaCenter(idAfpaCenter);
        // Retourne la liste des intendants avec un statut HTTP 200 (OK)
        return ResponseEntity.ok(intendants);
    }


    // Endpoint pour mettre à jour un intendant par son ID
    @PutMapping("/update/{id}")
    @Operation(summary = "Update intendant by ID")
    public ResponseEntity<IntendantDTO> updateIntendant(@PathVariable("id") Integer idIntendant, @Valid @RequestBody IntendantDTO intendantDto) {
        // Appel du service pour mettre à jour l'intendant existant avec les nouvelles données
        Optional<IntendantDTO> updatedIntendant = intendantService.update(idIntendant, intendantDto);

        // Retourne l'intendant mis à jour avec un statut HTTP 200 (OK)
        return updatedIntendant.map(ResponseEntity::ok)
                // Retourne une réponse avec un statut HTTP 404 (Not Found) si l'intendant n'est pas trouvé
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    // Endpoint pour supprimer un intendant par son ID
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete intendant by ID")
    public ResponseEntity<HttpStatus> deleteIntendant(@PathVariable("id") Integer idIntendant) {
        // Appel du service pour supprimer l'intendant par son ID
        intendantService.deleteById(idIntendant);
        // Retourne un statut HTTP 204 (No Content) après suppression
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
