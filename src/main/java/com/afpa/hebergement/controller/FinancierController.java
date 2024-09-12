package com.afpa.hebergement.controller;

import com.afpa.hebergement.model.dto.FinancierDTO;
import com.afpa.hebergement.service.entity_service.FinancierService;
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

@Tag(name = "Financier" , description = "Endpoints for financier")
@RestController
@RequestMapping("/api/financiers")
@AllArgsConstructor
@Validated
public class FinancierController {
    private final FinancierService financierService;

    @PostMapping("/create")
    @Operation
    public ResponseEntity<FinancierDTO> createFinancier(@Valid @RequestBody FinancierDTO financier) {
        FinancierDTO createdFinancier = financierService.create(financier);
        return ResponseEntity.ok(createdFinancier);
    }

    @GetMapping
    @Operation(summary = "Get all financiers")
    public ResponseEntity<List<FinancierDTO>> getAllFinanciers() {
        List<FinancierDTO> financiers = financierService.getAll();
        return ResponseEntity.ok(financiers);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get financier by ID")
    public ResponseEntity<FinancierDTO> getFinancierById(@PathVariable("id") Integer id) {
        Optional<FinancierDTO> financier = financierService.getById(id);
        return financier.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/delete/{id}")
    @Operation(summary = "Update financier by id")
    public ResponseEntity<HttpStatus> updateFinancier(@PathVariable("id") Integer id, @Valid @RequestBody FinancierDTO financier) {
        financierService.deleteById(financier.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
