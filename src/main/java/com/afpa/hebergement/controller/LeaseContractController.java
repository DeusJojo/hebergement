package com.afpa.hebergement.controller;

import com.afpa.hebergement.model.dto.LeaseContractDTO;
import com.afpa.hebergement.model.dto.LeaseContractFormDTO;
import com.afpa.hebergement.service.entity_service.LeaseContractService;
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


@Tag(name = "LeaseContract", description = "Endpoints to manage lease contract")
@RestController
@RequestMapping("/api/lease-contracts")
@AllArgsConstructor
@Validated
public class LeaseContractController {


    private final LeaseContractService leaseContractService;


    // Endpoint pour créer un nouveau contrat de bail
    @PostMapping("/create")
    @Operation(summary = "Create new lease contract")
    public ResponseEntity<LeaseContractDTO> createLeaseContract(@Valid @RequestBody LeaseContractFormDTO leaseContractFormDto) {
            // Appel du service pour créer un nouveau contrat de bail à partir du DTO
            LeaseContractDTO createdLeaseContract = leaseContractService.create(leaseContractFormDto);
            // Retourne le contrat de bail créé avec un statut HTTP 201 (Created)
            return new ResponseEntity<>(createdLeaseContract, HttpStatus.CREATED);
    }


    // Endpoint pour récupérer tous les contrats de bail par centre Afpa
    @GetMapping("/afpa-center/{idAfpaCenter}")
    @Operation(summary = "Get all lease contracts by Afpa Center")
    public ResponseEntity<List<LeaseContractDTO>> getAllByAfpaCenter(@PathVariable("idAfpaCenter") Integer idAfpaCenter) {
        // Appel du service pour obtenir tous les contrats de bail
        List<LeaseContractDTO> leaseContracts = leaseContractService.getAllByAfpaCenter(idAfpaCenter);
        // Retourne la liste des contrats de bail avec un statut HTTP 200 (OK)
        return ResponseEntity.ok(leaseContracts);
    }


    // Endpoint pour récupérer un contrat de location par son ID
    @GetMapping("/{id}")
    @Operation(summary = "Get LeaseContract by ID")
    public ResponseEntity<LeaseContractDTO> getLeaseContractById(@PathVariable("id") Integer id) {
        // Appel du service pour obtenir un contrat de location par son ID
        Optional<LeaseContractDTO> leaseContract = leaseContractService.getById(id);
        // Vérifie si le contrat de location est présent et retourne le contrat avec un statut HTTP 200 (OK)
        return leaseContract.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    // Endpoint pour supprimer un contrat de location par son ID
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete LeaseContract by ID")
    public ResponseEntity<HttpStatus> deleteLeaseContract(@PathVariable("id") Integer id) {
        // Appel du service pour supprimer un contrat de location par son ID
        leaseContractService.deleteById(id);
        // Retourne un statut HTTP 204 (No Content) après suppression
        return ResponseEntity.noContent().build();
    }


}
