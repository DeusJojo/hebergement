package com.afpa.hebergement.controller;

import com.afpa.hebergement.model.dto.BillDTO;
import com.afpa.hebergement.model.dto.BillFormDTO;
import com.afpa.hebergement.service.entity_service.BillService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.View;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/factures")
@Validated
public class BillController {

    private final BillService billService;

    // Endpoint pour créer une nouvelle facture
    @PostMapping("/create")
    public ResponseEntity<BillDTO> createBill(@Valid @RequestBody BillFormDTO billFormDTO){
        // Appel du service pour créer une nouvelle facture à partir du DTO
        BillDTO createdBill = billService.create(billFormDTO);
        // Retourne la facture créée avec un statut HTTP 201 (Created)
        return new ResponseEntity<>(createdBill, HttpStatus.CREATED);
    }

    // Endpoint pour récupérer une facture par son ID
    @GetMapping("/{id}")
    public ResponseEntity<BillDTO> getBillById(@PathVariable("id") Integer id){
        // Appel du service pour obtenir une facture par son ID
        Optional<BillDTO> bill = billService.getById(id);
        // Vérifie si la facture est présente et retourne la facture avec un statut HTTP 200 (OK)
        return bill.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Endpoint pour supprimer une facture par son ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteBill(@PathVariable("id") Integer id){
        // Appel du service pour supprimer une facture par son ID
        billService.deleteById(id);
        // Retourne un statut HTTP 204 (No Content) après suppression
        return ResponseEntity.noContent().build();
    }

}
