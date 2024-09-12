package com.afpa.hebergement.controller;

import com.afpa.hebergement.model.dto.DepositDTO;
import com.afpa.hebergement.service.entity_service.DepositService;
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


@Tag(name = "Deposit" ,description = "Endpoint for Deposit")
@RestController
@RequestMapping("/api/deposits")
@AllArgsConstructor
@Validated
public class DepositController {


    private final DepositService depositService;


    @PostMapping("/create")
    @Operation
    public ResponseEntity<DepositDTO> createDeposit(@Valid @RequestBody DepositDTO deposit) {
        DepositDTO createdDeposit = depositService.create(deposit);
        return ResponseEntity.ok(createdDeposit);
    }


    @GetMapping
    @Operation(summary = "Get all floor")
    public ResponseEntity<List<DepositDTO>> getAllDeposits() {
        List<DepositDTO> deposits = depositService.getAll();
        return ResponseEntity.ok(deposits);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get floor by ID")
    public ResponseEntity<DepositDTO> getDepositById(@PathVariable("id") Integer id) {
        Optional<DepositDTO> deposit = depositService.getById(id);
        return deposit.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/delete/{id}")
    @Operation(summary = "Update deposit by id")
    public ResponseEntity<HttpStatus> updateDeposit(@PathVariable("id") Integer id, @Valid @RequestBody DepositDTO deposit) {
        depositService.deleteById(deposit.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
