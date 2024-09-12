package com.afpa.hebergement.controller;

import com.afpa.hebergement.model.dto.RentDTO;
import com.afpa.hebergement.service.entity_service.RentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * Contrôleur pour gérer les opérations liées aux loyers.
 */
@Tag(name = "Rent Controller", description = "Points de terminaison pour gérer les locations")
@RestController
@AllArgsConstructor
@RequestMapping("/api/rents")
public class RentController {

    private final RentService rentService;

    /**
     * Point de terminaison pour créer un nouveau loyer.
     *
     * @param rentDto L'objet RentDTO contenant les détails du loyer.
     * @return ResponseEntity contenant le RentDTO créé et le statut HTTP.
     */
    @Operation(description = "Create a rent")
    @PostMapping("/create")
    public ResponseEntity<RentDTO> create(@RequestBody RentDTO rentDto) {
        RentDTO savedRentDto = rentService.create(rentDto);
        return new ResponseEntity<>(savedRentDto, HttpStatus.CREATED);
    }

    /**
     * Point de terminaison pour obtenir tous les loyers.
     *
     * @return ResponseEntity contenant une liste de tous les RentDTOs et le statut HTTP.
     */
    @Operation(description = "Get all rents")
    @GetMapping
    public ResponseEntity<List<RentDTO>> getAll() {
        List<RentDTO> rents = rentService.getAll();
        return new ResponseEntity<>(rents, HttpStatus.OK);
    }

    /**
     * Récupère tous les loyers associés à un centre AFPA spécifique via son identifiant.
     *
     * @param idAfpaCenter L'identifiant du centre AFPA pour lequel récupérer les loyers.
     * @return ResponseEntity contenant une liste de tous les RentDTOs et le statut HTTP.
     */
    @Operation(description = "Get all rents by afpa center id")
    @GetMapping("/getAllByIdAfpaCenter/{id}")
    public ResponseEntity<List<RentDTO>> getAllByAfpaCenterId(@PathVariable("id") Integer idAfpaCenter) {
        List<RentDTO> rentDtoList = rentService.getAllByAfpaCenterId(idAfpaCenter);
        return new ResponseEntity<>(rentDtoList, HttpStatus.OK);
    }

    /**
     * Point de terminaison pour obtenir un loyer par son identifiant.
     *
     * @param id L'identifiant du loyer.
     * @return ResponseEntity contenant le RentDTO correspondant et le statut HTTP, ou le statut HTTP NOT FOUND si le loyer n'est pas trouvé.
     */
    @Operation(description = "Get a rent by id")
    @GetMapping("{id}")
    public ResponseEntity<RentDTO> getById(@PathVariable Integer id) {
        Optional<RentDTO> rentDtoOptional = rentService.getById(id);
        return rentDtoOptional.map(rentDto -> new ResponseEntity<>(rentDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Point de terminaison pour mettre à jour un loyer.
     *
     * @param id L'identifiant du loyer à mettre à jour.
     * @param rentDto L'objet RentDTO contenant les nouveaux détails du loyer.
     * @return ResponseEntity contenant le RentDTO mis à jour et le statut HTTP, ou le statut HTTP NOT FOUND si le loyer n'est pas trouvé.
     */
    @Operation(description = "Update a rent")
    @PutMapping("/update/{id}")
    public ResponseEntity<RentDTO> update(@PathVariable("id") Integer id, @RequestBody RentDTO rentDto) {
        Optional<RentDTO> updatedRentDtoOptional = rentService.update(id, rentDto);
        return updatedRentDtoOptional.map(updatedRentDto -> new ResponseEntity<>(updatedRentDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Point de terminaison pour supprimer un loyer.
     *
     * @param id L'identifiant du loyer à supprimer.
     * @return ResponseEntity avec le statut HTTP NO CONTENT.
     */
    @Operation(description = "Delete a rent")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable("id") Integer id) {
        rentService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
