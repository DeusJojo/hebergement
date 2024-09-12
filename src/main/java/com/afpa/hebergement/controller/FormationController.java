package com.afpa.hebergement.controller;

import com.afpa.hebergement.model.dto.FormationDTO;
import com.afpa.hebergement.service.entity_service.FormationService;
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

@Tag(name = "Formation", description = "Endpoint for Formatione")
@RestController
@RequestMapping("/api/formations")
@AllArgsConstructor
@Validated
public class FormationController {


    private final FormationService formationService;


    @PostMapping("/create")
    @Operation
    public ResponseEntity<FormationDTO> createFormation(@Valid @RequestBody FormationDTO formation) {
        FormationDTO createdFormation = formationService.create(formation);
        return ResponseEntity.ok(createdFormation);
    }

    @GetMapping
    @Operation(summary = "Get all formation")
    public ResponseEntity<List<FormationDTO>> getAllFormations() {
        List<FormationDTO> formations = formationService.getAll();
        return ResponseEntity.ok(formations);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get formation by ID")
    public ResponseEntity<FormationDTO> getFormationById(@PathVariable("id") Integer id) {
        Optional<FormationDTO> formation = formationService.getById(id);
        return formation.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete formation by id")
    public ResponseEntity<HttpStatus> deleteFormation(@PathVariable("id") Integer id) {
        formationService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<FormationDTO> updateFormation(@PathVariable("id") Integer id, @RequestBody FormationDTO formationDTO) {
        Optional<FormationDTO> updatedFormationOpt = formationService.update(id, formationDTO);
        return updatedFormationOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

}
