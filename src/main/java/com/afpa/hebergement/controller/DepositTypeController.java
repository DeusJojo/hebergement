package com.afpa.hebergement.controller;


import com.afpa.hebergement.model.dto.DepositTypeDTO;
import com.afpa.hebergement.service.entity_service.DepositTypeService;
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

@Tag(name = "DepositType" , description = "Endpoint for Deposit Type")
@RestController
@RequestMapping("/api/depositTypes")
@AllArgsConstructor
@Validated
public class DepositTypeController {

    private final DepositTypeService depositTypeService;

    @PostMapping("/create")
    @Operation
    public ResponseEntity<DepositTypeDTO> create(@Valid @RequestBody DepositTypeDTO depositType) {
        DepositTypeDTO createdDepositType = depositTypeService.create(depositType);
        return ResponseEntity.ok(createdDepositType);
    }

    @GetMapping
    @Operation(summary = "Get all floor")
    public ResponseEntity<List<DepositTypeDTO>> getAllDepositTypes() {
        List<DepositTypeDTO> depositTypes = depositTypeService.getAll();
        return ResponseEntity.ok(depositTypes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get floor by ID")
    public ResponseEntity<DepositTypeDTO> getDepositTypeById(@PathVariable("id") Integer id) {
        Optional<DepositTypeDTO> depositTypes = depositTypeService.getById(id);
        return depositTypes.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/delete/{id}")
    @Operation(summary = "Update deposit by id")
    public ResponseEntity<HttpStatus> updateDepositType(@PathVariable("id") Integer id, @Valid @RequestBody DepositTypeDTO depositTypes) {
        depositTypeService.deleteById(depositTypes.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
