package com.afpa.hebergement.controller;

import com.afpa.hebergement.model.dto.SessionFormationDTO;
import com.afpa.hebergement.service.entity_service.SessionFormationService;
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

@Tag(name = "SessionFormation")
@RestController
@RequestMapping("/api/sessionFormation")
@AllArgsConstructor
@Validated
public class SessionFormationController {

    private final SessionFormationService sessionFormationService;


    @PostMapping("/create")
    @Operation
    public ResponseEntity<SessionFormationDTO> createSessionFormation(@Valid @RequestBody SessionFormationDTO sessionFormation) {
        SessionFormationDTO createdSessionFormation = sessionFormationService.create(sessionFormation);
        return ResponseEntity.ok(createdSessionFormation);
    }

    @GetMapping
    @Operation(summary = "Get all SessionFormation")
    public ResponseEntity<List<SessionFormationDTO>> getAllSessionFormations() {
        List<SessionFormationDTO> sessionFormations = sessionFormationService.getAll();
        return ResponseEntity.ok(sessionFormations);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get SessionFormation by ID")
    public ResponseEntity<SessionFormationDTO> getSessionFormationById(@PathVariable("id") Integer id) {
        Optional<SessionFormationDTO> sessionFormation = sessionFormationService.getById(id);
        return sessionFormation.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Update sessionFormation by id")
    public ResponseEntity<HttpStatus> deleteSessionFormation(@PathVariable("id") Integer id) {
        sessionFormationService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SessionFormationDTO> updateSessionFormation(@PathVariable("id") Integer id, @RequestBody SessionFormationDTO sessionFormationDTO) {
        Optional<SessionFormationDTO> updatedSessionFormationOpt = sessionFormationService.update(id, sessionFormationDTO);
        return updatedSessionFormationOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
