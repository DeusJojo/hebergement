package com.afpa.hebergement.controller;

import com.afpa.hebergement.model.dto.WorkDTO;
import com.afpa.hebergement.service.entity_service.WorkService;
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

@Tag(name = "Work", description = "Endpoints to manage works")
@RestController
@RequestMapping("/api/works")
@AllArgsConstructor
public class WorkController {


    private final WorkService workService;


    // Endpoint pour créer un nouveau travail
    @PostMapping("/create")
    @Operation(summary = "Create new work")
    public ResponseEntity<WorkDTO> createWork(@Valid @RequestBody WorkDTO workDto) {
        // Appel du service pour créer un nouveau travail à partir du DTO
        WorkDTO createdWork = workService.create(workDto);
        // Retourne le travail créé avec un statut HTTP 201 (Created)
        return new ResponseEntity<>(createdWork, HttpStatus.CREATED);
    }


    // Endpoint pour récupérer tous les travaux
    @GetMapping
    @Operation(summary = "Get all works")
    public ResponseEntity<List<WorkDTO>> getAllWorks() {
        // Appel du service pour obtenir tous les travaux
        List<WorkDTO> works = workService.getAll();
        // Retourne la liste des travaux avec un statut HTTP 200 (OK)
        return ResponseEntity.ok(works);
    }


    // Endpoint pour récupérer les travaux avec pagination
    @GetMapping("/page")
    @Operation(summary = "Get works with pagination")
    public ResponseEntity<Page<WorkDTO>> getWorksPage(
            // Définit un paramètre de requête "page" avec une valeur par défaut de 0 (1ère page).
            @RequestParam(defaultValue = "0") int page,
            // Définit un paramètre de requête "size" avec une valeur par défaut de 5 (5 éléments par page).
            @RequestParam(defaultValue = "5") int size) {

        // Appel du service pour obtenir les travaux avec pagination en utilisant les paramètres de page et de taille.
        Page<WorkDTO> worksPage = workService.getAll(page, size);

        // Retourne la page de travaux avec un statut HTTP 200 (OK)
        return ResponseEntity.ok(worksPage);
    }


    // Endpoint pour récupérer un travail par son ID
    @GetMapping("/{id}")
    @Operation(summary = "Get work by ID")
    public ResponseEntity<WorkDTO> getWorkById(@PathVariable("id") Integer idWork) {
        // Appel du service pour obtenir un travail par son ID
        Optional<WorkDTO> work = workService.getById(idWork);

        // Retourne le travail avec un statut HTTP 200 (OK) s'il est présent
        return work.map(ResponseEntity::ok)
                // Retourne une réponse avec un statut HTTP 404 (Not Found) si l'entité Work n'est pas trouvée
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Endpoint pour récupérer les travaux par centre Afpa
    @GetMapping("/afpa-center/{idAfpaCenter}")
    @Operation(summary = "Get works by Afpa Center")
    public ResponseEntity<List<WorkDTO>> getWorksByAfpaCenter(@PathVariable("idAfpaCenter") Integer idAfpaCenter) {
        // Appel du service pour obtenir tous les travaux par centre afpa
        List<WorkDTO> works = workService.getWorksByAfpaCenter(idAfpaCenter);
        // Retourne la liste de travaux avec un statut HTTP 200 (OK)
        return ResponseEntity.ok(works);
    }


    // Endpoint pour mettre à jour un travail par son ID
    @PutMapping("/update/{id}")
    @Operation(summary = "Update work by ID")
    public ResponseEntity<WorkDTO> updateWork(@PathVariable("id") Integer idWork, @Valid @RequestBody WorkDTO workDto) {
        // Appel du service pour mettre à jour le travail existant avec les nouvelles données
        Optional<WorkDTO> updatedWork = workService.update(idWork, workDto);

        // Retourne le travail mis à jour avec un statut HTTP 200 (OK) s'il est présent
        return updatedWork.map(ResponseEntity::ok)
                // Retourne une réponse avec un statut HTTP 404 (Not Found) si l'entité Work n'est pas trouvée après la mise à jour
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Endpoint pour supprimer un travail par son ID
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete work by ID")
    public ResponseEntity<HttpStatus> deleteWork(@PathVariable("id") Integer idWork) {
        // Appel du service pour supprimer le travail par son ID
        workService.deleteById(idWork);
        // Retourne un statut HTTP 204 (No Content) après suppression
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
