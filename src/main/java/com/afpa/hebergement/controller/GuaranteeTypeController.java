package com.afpa.hebergement.controller;

import com.afpa.hebergement.model.dto.GuaranteeTypeDTO;
import com.afpa.hebergement.service.entity_service.GuaranteeTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Classe de contrôleur pour gérer les types de garanties.
 */
@Tag(name = "Guarantee Type Controller", description = "Endpoints to manage guarantee type")
@RestController
@AllArgsConstructor
@RequestMapping("/api/guaranties-type")
public class GuaranteeTypeController {

    private final GuaranteeTypeService guaranteeTypeService;

    /**
     * Endpoint pour créer un nouveau type de garantie.
     *
     * @param guaranteeTypeDto Le DTO du type de garantie
     * @return ResponseEntity contenant le DTO du type de garantie créé et le statut HTTP
     */
    @Operation(description = "Create a guarantee type")
    @PostMapping("/create")
    public ResponseEntity<GuaranteeTypeDTO> create(@RequestBody GuaranteeTypeDTO guaranteeTypeDto) {
        GuaranteeTypeDTO savedGuaranteeTypeDto = guaranteeTypeService.create(guaranteeTypeDto);
        return new ResponseEntity<>(savedGuaranteeTypeDto, HttpStatus.CREATED);
    }

    /**
     * Endpoint pour récupérer tous les types de garanties.
     *
     * @return ResponseEntity contenant la liste de tous les DTO de types de garanties et le statut HTTP
     */
    @Operation(description = "Get all guarantee type")
    @GetMapping
    public ResponseEntity<List<GuaranteeTypeDTO>> getAll() {
        List<GuaranteeTypeDTO> guaranteeTypeDtoList = guaranteeTypeService.getAll();
        return new ResponseEntity<>(guaranteeTypeDtoList, HttpStatus.OK);
    }

    /**
     * Endpoint pour récupérer un type de garantie par son ID.
     *
     * @param id L'ID du type de garantie
     * @return ResponseEntity contenant le DTO du type de garantie et le statut HTTP
     */
    @Operation(description = "Get guarantee type by id")
    @GetMapping("/{id}")
    public ResponseEntity<GuaranteeTypeDTO> getById(@PathVariable Integer id) {
        Optional<GuaranteeTypeDTO> guaranteeTypeDto = guaranteeTypeService.getById(id);
        return guaranteeTypeDto.map(guaranteeType -> new ResponseEntity<>(guaranteeType, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint pour mettre à jour un type de garantie.
     *
     * @param id L'ID du type de garantie à mettre à jour
     * @param guaranteeTypeDto Les nouvelles données pour le type de garantie
     * @return ResponseEntity contenant le DTO du type de garantie mis à jour et le statut HTTP
     */
    @Operation(description = "Update a guarantee type")
    @PutMapping("/update/{id}")
    public ResponseEntity<GuaranteeTypeDTO> update(@PathVariable("id") Integer id,
                                                   @RequestBody GuaranteeTypeDTO guaranteeTypeDto) {
        Optional<GuaranteeTypeDTO> updatedGuaranteeDtoOptional = guaranteeTypeService.update(id, guaranteeTypeDto);
        return updatedGuaranteeDtoOptional.map(updatedGuaranteeDto -> new ResponseEntity<>(updatedGuaranteeDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint pour supprimer un type de garantie par son ID.
     *
     * @param id L'ID du type de garantie à supprimer
     * @return ResponseEntity avec le statut HTTP
     */
    @Operation(description = "Delete a guarantee type")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable("id") Integer id){
        guaranteeTypeService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
