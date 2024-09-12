package com.afpa.hebergement.controller;

import com.afpa.hebergement.model.dto.WorkTypeDTO;
import com.afpa.hebergement.service.entity_service.WorkTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Work type", description = "Endpoints to manage Work Type")
@RestController
@RequestMapping("/api/work-types")
@AllArgsConstructor
public class WorkTypeController {


    private final WorkTypeService workTypeService;

    // Endpoint pour créer un nouveau type de travail
    @PostMapping("/create")
    @Operation(summary = "Create new work type")
    public ResponseEntity<WorkTypeDTO> createWorkType(@Valid @RequestBody WorkTypeDTO workTypeDto) {
        // Appel du service pour créer un nouveau type de travail à partir du DTO
        WorkTypeDTO createdWorkType = workTypeService.create(workTypeDto);
        // Retourne le type de travail créé avec un statut HTTP 201 (Created)
        return new ResponseEntity<>(createdWorkType, HttpStatus.CREATED);
    }

    // Endpoint pour récupérer tous les types de travail
    @GetMapping
    @Operation(summary = "Get all work types")
    public ResponseEntity<List<WorkTypeDTO>> getAllWorkTypes() {
        // Appel du service pour obtenir tous les types de travail
        List<WorkTypeDTO> workTypes = workTypeService.getAll();
        // Retourne la liste des types de travail avec un statut HTTP 200 (OK)
        return ResponseEntity.ok(workTypes);
    }


    // Endpoint pour récupérer un type de travail par son ID
    @GetMapping("/{id}")
    @Operation(summary = "Get work type by ID")
    public ResponseEntity<WorkTypeDTO> getWorkTypeById(@PathVariable("id") Integer idWorkType) {
        // Appel du service pour obtenir un type de travail par son ID
        Optional<WorkTypeDTO> workType = workTypeService.getById(idWorkType);

        // Retourne le type de travail avec un statut HTTP 200 (OK) si présent
        return workType.map(ResponseEntity::ok)
                // Retourne une réponse avec un statut HTTP 404 (Not Found) si le type de travail n'est pas trouvé
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Endpoint pour mettre à jour un type de travail par son ID
    @PutMapping("/update/{id}")
    @Operation(summary = "Update work type by ID")
    public ResponseEntity<WorkTypeDTO> updateWorkType(@PathVariable("id") Integer idWorkType, @Valid @RequestBody WorkTypeDTO workTypeDto) {
        // Appel du service pour mettre à jour le type de travail existant avec les nouvelles données
        Optional<WorkTypeDTO> updatedWorkType = workTypeService.update(idWorkType, workTypeDto);

        // Retourne le type de travail mis à jour avec un statut HTTP 200 (OK) si présent
        return updatedWorkType.map(ResponseEntity::ok)
                // Retourne une réponse avec un statut HTTP 404 (Not Found) si le type de travail n'est pas trouvé après la mise à jour
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Endpoint pour supprimer un type de travail par son ID
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete work type by ID")
    public ResponseEntity<HttpStatus> deleteWorkType(@PathVariable("id") Integer idWorkType) {
        // Appel du service pour supprimer le type de travail par son ID
        workTypeService.deleteById(idWorkType);
        // Retourne un statut HTTP 204 (No Content) après suppression
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
