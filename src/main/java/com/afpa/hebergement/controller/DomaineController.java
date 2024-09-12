package com.afpa.hebergement.controller;

import com.afpa.hebergement.model.dto.DomaineDTO;
import com.afpa.hebergement.model.entity.Domaine;
import com.afpa.hebergement.model.mapper.DomaineMapper;
import com.afpa.hebergement.service.entity_service.DomaineService;
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

@Tag(name = "Domaine", description = "Endpoints for domaines")
@RestController
@RequestMapping("/api/domaines")
@AllArgsConstructor
@Validated
public class DomaineController {

    private final DomaineService domaineService;

    @PostMapping("/create")
    @Operation
    public ResponseEntity<DomaineDTO> create(@Valid @RequestBody Domaine domaine) {
        DomaineDTO createdDomaine = domaineService.create(DomaineMapper.mapToDomaineDTO(domaine));
        return ResponseEntity.ok(createdDomaine);
    }

    @GetMapping
    @Operation(summary = "Get all domaines")
    public ResponseEntity<List<DomaineDTO>> getAllDomaines() {
        List<DomaineDTO> domaines = domaineService.getAll();
        return ResponseEntity.ok(domaines);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get domaines by id")
    public ResponseEntity<DomaineDTO> getDomaineById(@PathVariable("id") Integer id) {
        Optional<DomaineDTO> domaine = domaineService.getById(id);
        return domaine.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    @Operation
    public ResponseEntity<HttpStatus> deleteDomaine(@PathVariable("id") Integer id) {
        domaineService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{id}")
    @Operation
    public ResponseEntity<DomaineDTO> updateDomaine(@PathVariable("id") Integer id, @Valid @RequestBody DomaineDTO domaine) {
        Optional<DomaineDTO> updatedDomaine = domaineService.update(id, domaine);
        return updatedDomaine.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
