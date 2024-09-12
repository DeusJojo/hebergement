package com.afpa.hebergement.controller;

import com.afpa.hebergement.model.dto.GuaranteeDTO;
import com.afpa.hebergement.service.entity_service.GuaranteeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Classe de contrôleur pour gérer les garanties.
 */
@Tag(name = "Guarantee Controller", description = "Endpoints to manage guaranties")
@RestController
@AllArgsConstructor
@RequestMapping("/api/guaranties")
public class GuaranteeController {

    private final GuaranteeService guaranteeService;

    /**
     * Endpoint pour créer une nouvelle garantie.
     *
     * @param guaranteeDto Le DTO de la garantie à créer
     * @return ResponseEntity contenant le DTO de la garantie créée et le statut HTTP
     */
    @Operation(description = "Create a guarantee")
    @PostMapping("/create")
    public ResponseEntity<GuaranteeDTO> create(@RequestBody GuaranteeDTO guaranteeDto) {
        GuaranteeDTO savedGuaranteeDto = guaranteeService.create(guaranteeDto);
        return new ResponseEntity<>(savedGuaranteeDto, HttpStatus.CREATED);
    }

    /**
     * Endpoint pour récupérer toutes les garanties.
     *
     * @return ResponseEntity contenant la liste de tous les DTO de garanties et le statut HTTP
     */
    @Operation(description = "Get all guaranties")
    @GetMapping
    public ResponseEntity<List<GuaranteeDTO>> getAll() {
        List<GuaranteeDTO> guaranteeDtoList = guaranteeService.getAll();
        return new ResponseEntity<>(guaranteeDtoList, HttpStatus.OK);
    }

    /**
     * Endpoint pour récupérer toutes les garanties d'un centre afpa.
     *
     * @return ResponseEntity contenant la liste de tous les DTO de garanties d'un centre afpa et le statut HTTP
     */
    @Operation(description = "Get all guaranties by afpa center id")
    @GetMapping("/getAllByIdAfpaCenter/{id}")
    public ResponseEntity<List<GuaranteeDTO>> getAllByAfpaCenterId(@PathVariable("id") Integer idAfpaCenter) {
        List<GuaranteeDTO> guaranteeDtoList = guaranteeService.getAllByAfpaCenterId(idAfpaCenter);
        return new ResponseEntity<>(guaranteeDtoList, HttpStatus.OK);
    }

    /**
     * Endpoint pour récupérer une garantie par son ID.
     *
     * @param id L'ID de la garantie à récupérer
     * @return ResponseEntity contenant le DTO de la garantie et le statut HTTP
     */
    @Operation(description = "Get a guarantee by id")
    @GetMapping("/{id}")
    public ResponseEntity<GuaranteeDTO> getById(@PathVariable("id") Integer id) {
        Optional<GuaranteeDTO> guaranteeDtoptional = guaranteeService.getById(id);
        return guaranteeDtoptional.map(guaranteeDto -> new ResponseEntity<>(guaranteeDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint pour récupérer une garantie par le type de garantie.
     *
     * @param idGuaranteeType L'id type de garantie pour lequel récupérer le DTO
     * @return ResponseEntity contenant le DTO de la garantie et le statut HTTP
     */
    @Operation(summary = "Get a guarantee by guarantee type and afpa center", description = "Get a guarantee by guarantee type and afpa center")
    @GetMapping("/{idType}/{idAfpa}")
    public ResponseEntity<GuaranteeDTO> getByGuaranteeTypeAndAfpaCenter(@PathVariable("idType") Integer idGuaranteeType,
                                                                        @PathVariable("idAfpa") Integer idAfpaCenter) {
        Optional<GuaranteeDTO> guaranteeDtoptional = guaranteeService.
                getByIdGuaranteeTypeAndAfpaCenter(idGuaranteeType, idAfpaCenter);
        return guaranteeDtoptional.map(guaranteeDto -> new ResponseEntity<>(guaranteeDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint pour mettre à jour une garantie.
     *
     * @param id L'ID de la garantie à mettre à jour
     * @param guaranteeDto Les nouvelles données pour la garantie
     * @return ResponseEntity contenant le DTO de la garantie mise à jour et le statut HTTP
     */
    @Operation(description = "Update a guarantee")
    @PutMapping("/update/{id}")
    public ResponseEntity<GuaranteeDTO> update(@PathVariable("id") Integer id,
                                               @RequestBody GuaranteeDTO guaranteeDto) {
        Optional<GuaranteeDTO> updatedGuaranteeDtoOptional = guaranteeService.update(id, guaranteeDto);
        return updatedGuaranteeDtoOptional.map(updatedGuaranteePerson -> new ResponseEntity<>(updatedGuaranteePerson, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint pour supprimer une garantie par son ID.
     *
     * @param id L'ID de la garantie à supprimer
     * @return ResponseEntity avec le statut HTTP
     */
    @Operation(description = "Delete a guarantee")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable("id") Integer id) {
        guaranteeService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
