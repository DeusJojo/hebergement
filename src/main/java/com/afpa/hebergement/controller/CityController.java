package com.afpa.hebergement.controller;

import com.afpa.hebergement.model.dto.CityDTO;
import com.afpa.hebergement.service.entity_service.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Contrôleur pour la gestion des villes.
 * Il fournit des points d'entrée pour créer, récupérer et rechercher des villes.
 */
@Tag(name = "City Controller", description = "Endpoints to manage cities")
@RestController
@AllArgsConstructor
@RequestMapping("/api/cities")
public class CityController {

    private final CityService cityService;

    /**
     * Crée une nouvelle ville.
     * @param cityDto L'objet CityDTO contenant les détails de la ville à créer.
     * @return Une réponse HTTP avec l'objet CityDTO créé et un statut HTTP 201 (Created).
     */
    @Operation(description = "Create a city")
    @PostMapping("/create")
    public ResponseEntity<CityDTO> create(@RequestBody CityDTO cityDto) {
        CityDTO savedCityDto = cityService.create(cityDto);
        return new ResponseEntity<>(savedCityDto, HttpStatus.CREATED);
    }

    /**
     * Récupère une ville par son identifiant.
     * @param id L'identifiant de la ville à récupérer.
     * @return Une réponse HTTP avec l'objet CityDTO correspondant et un statut HTTP 200 (OK) si trouvé,
     * sinon un statut HTTP 404 (Not Found).
     */
    @Operation(description = "Get city by id")
    @GetMapping("/{id}")
    public ResponseEntity<CityDTO> getById(@PathVariable("id") Integer id) {
        Optional<CityDTO> cityDtoOptional = cityService.getById(id);
        return cityDtoOptional.map(cityDto -> new ResponseEntity<>(cityDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
