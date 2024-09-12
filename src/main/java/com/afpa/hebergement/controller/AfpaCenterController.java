package com.afpa.hebergement.controller;

import com.afpa.hebergement.model.dto.AfpaCenterDTO;
import com.afpa.hebergement.service.entity_service.AfpaCenterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Classe de contrôleur pour gérer les centres AFPA.
 */
@Tag(name = "Afpa Center Controller", description = "Endpoints pour gérer les centres AFPA")
@RestController
@AllArgsConstructor
@RequestMapping("/api/afpa-centers")
public class AfpaCenterController {

    private final AfpaCenterService afpaCenterService;

    /**
     * Endpoint pour créer un nouveau centre AFPA.
     *
     * @param afpaCenterDto Le DTO du centre AFPA
     * @return ResponseEntity contenant le DTO du centre AFPA créé et le statut HTTP
     */
    @Operation(description = "Create an afpa center")
    @PostMapping("/create")
    public ResponseEntity<AfpaCenterDTO> create(@RequestBody AfpaCenterDTO afpaCenterDto) {
        AfpaCenterDTO savedAfpaCenterDto = afpaCenterService.create(afpaCenterDto);
        return new ResponseEntity<>(savedAfpaCenterDto, HttpStatus.CREATED);
    }

    /**
     * Endpoint pour récupérer tous les centres AFPA.
     *
     * @return ResponseEntity contenant la liste de tous les DTO de centres AFPA et le statut HTTP
     */
    @Operation(description = "Get all afpa centers")
    @GetMapping
    public ResponseEntity<List<AfpaCenterDTO>> getAll() {
        List<AfpaCenterDTO> afpaCenterDtoList = afpaCenterService.getAll();
        return new ResponseEntity<>(afpaCenterDtoList, HttpStatus.OK);
    }

    /**
     * Endpoint pour récupérer un centre AFPA par son ID.
     *
     * @param id L'ID du centre AFPA
     * @return ResponseEntity contenant le DTO du centre AFPA et le statut HTTP
     */
    @Operation(description = "Get a document center by id")
    @GetMapping("/{id}")
    public ResponseEntity<AfpaCenterDTO> getById(@PathVariable("id") Integer id) {
        Optional<AfpaCenterDTO> afpaCenterDtoOptional = afpaCenterService.getById(id);
        return afpaCenterDtoOptional.map(afpaCenterDto -> new ResponseEntity<>(afpaCenterDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint pour récupérer un centre AFPA par son nom.
     *
     * @param centerName Le nom du centre AFPA
     * @return ResponseEntity contenant le DTO du centre AFPA et le statut HTTP
     */
    @Operation(description = "Get an afpa center by center name")
    @GetMapping("/getByCenterName/{centerName}")
    public ResponseEntity<AfpaCenterDTO> getByCenterName(@PathVariable("centerName") String centerName) {
        Optional<AfpaCenterDTO> afpaCenterDtoOptional = afpaCenterService.getByCenterName(centerName);
        return afpaCenterDtoOptional.map(afpaCenterDto -> new ResponseEntity<>(afpaCenterDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint pour récupérer un centre AFPA par le nom de la ville.
     *
     * @param cityName Le nom de la ville où se trouve le centre AFPA
     * @return ResponseEntity contenant le DTO du centre AFPA et le statut HTTP
     */
    @Operation(description = "Get an afpa center by city name")
    @GetMapping("/getByCityName/{cityName}")
    public ResponseEntity<AfpaCenterDTO> getByCityName(@PathVariable("cityName") String cityName) {
        Optional<AfpaCenterDTO> afpaCenterDtoOptional = afpaCenterService.getByCityName(cityName);
        return afpaCenterDtoOptional.map(afpaCenterDto -> new ResponseEntity<>(afpaCenterDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint pour mettre à jour un centre AFPA.
     *
     * @param id L'ID du centre AFPA à mettre à jour
     * @param afpaCenterDto Les nouvelles données pour le centre AFPA
     * @return ResponseEntity contenant le DTO du centre AFPA mis à jour et le statut HTTP
     */
    @Operation(description = "Update an afpa center")
    @PutMapping("/update/{id}")
    public ResponseEntity<AfpaCenterDTO> update(@PathVariable("id") Integer id,
                                                @RequestBody AfpaCenterDTO afpaCenterDto) {
        Optional<AfpaCenterDTO> updatedAfpaCenterDtoOptional = afpaCenterService.update(id, afpaCenterDto);
        return updatedAfpaCenterDtoOptional.map(updatedAfpaCenterDto -> new ResponseEntity<>(updatedAfpaCenterDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint pour supprimer un centre AFPA par son ID.
     *
     * @param id L'ID du centre AFPA à supprimer
     * @return ResponseEntity avec le statut HTTP
     */
    @Operation(description = "Delete an afpa center")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable("id") Integer id){
        afpaCenterService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
