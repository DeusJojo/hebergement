package com.afpa.hebergement.controller;

import com.afpa.hebergement.model.dto.RegionDTO;
import com.afpa.hebergement.service.entity_service.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Contrôleur pour gérer les régions.
 */
@Tag(name = "Region Controller", description = "Endpoints to manage regions")
@RestController
@AllArgsConstructor
@RequestMapping("/api/regions")
public class RegionController {

    private final RegionService regionService;

    /**
     * Crée une nouvelle région.
     *
     * @param regionDto Les informations de la région à créer.
     * @return La région créée avec un statut HTTP 201 (Created).
     */
    @Operation(
            description = "Create a region"
    )
    @PostMapping("/create")
    public ResponseEntity<RegionDTO> create(@RequestBody RegionDTO regionDto) {
        RegionDTO savedRegionDto = regionService.create(regionDto);
        return new ResponseEntity<>(savedRegionDto, HttpStatus.CREATED);
    }

    /**
     * Récupère une région par son identifiant.
     *
     * @param id L'identifiant de la région à récupérer.
     * @return La région correspondante avec un statut HTTP 200 (OK) si trouvée, sinon un statut HTTP 404 (Not Found).
     */
    @Operation(
            description = "Get a region by id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<RegionDTO> getById(@PathVariable("id") Integer id) {
        Optional<RegionDTO> regionDtoOptional = regionService.getById(id);
        return regionDtoOptional.map(regionDto -> new ResponseEntity<>(regionDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Récupère une région par son code de région.
     *
     * @param name Le code de la région à récupérer.
     * @return La région correspondante avec un statut HTTP 200 (OK) si trouvée, sinon un statut HTTP 404 (Not Found).
     */
    @Operation(
            description = "Get a region by region code"
    )
    @GetMapping("/getByRegionName/{name}")
    public ResponseEntity<RegionDTO> getByRegionName(@PathVariable("name") String name) {
        Optional<RegionDTO> regionDtoOptional = regionService.getByRegionName(name);
        return regionDtoOptional.map(regionDto -> new ResponseEntity<>(regionDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
