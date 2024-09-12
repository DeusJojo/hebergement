package com.afpa.hebergement.controller;

import com.afpa.hebergement.model.dto.DepartmentDTO;
import com.afpa.hebergement.service.entity_service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Contrôleur pour gérer les départements.
 */
@Tag(name = "Department Controller", description = "Endpoints to manage departements")
@RestController
@AllArgsConstructor
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    /**
     * Endpoint pour créer un département.
     *
     * @param departmentDto DTO représentant le département à créer
     * @return ResponseEntity avec le DTO du département créé et le statut HTTP 201 CREATED
     */
    @Operation(description = "Create a departement")
    @PostMapping("/create")
    public ResponseEntity<DepartmentDTO> create(@RequestBody DepartmentDTO departmentDto) {
        DepartmentDTO savedDepartmentDto = departmentService.create(departmentDto);
        return new ResponseEntity<>(savedDepartmentDto, HttpStatus.CREATED);
    }

    /**
     * Endpoint pour récupérer un département par son identifiant.
     *
     * @param id Identifiant du département à récupérer
     * @return ResponseEntity avec le DTO du département trouvé et le statut HTTP 200 OK,
     * ou statut HTTP 404 NOT FOUND si le département n'est pas trouvé
     */
    @Operation(description = "Get a department by id")
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getById(@PathVariable("id") Integer id) {
        Optional<DepartmentDTO> departmentDtoOptional = departmentService.getById(id);
        return departmentDtoOptional.map(departmentDTO -> new ResponseEntity<>(departmentDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint pour récupérer un département par son code.
     *
     * @param code Code du département à récupérer
     * @return ResponseEntity avec le DTO du département trouvé et le statut HTTP 200 OK,
     * ou statut HTTP 404 NOT FOUND si le département n'est pas trouvé
     */
    @Operation(description = "Get a department by code")
    @GetMapping("/getByDepartmentCode/{code}")
    public ResponseEntity<DepartmentDTO> getDepartmentByCode(@PathVariable("code") String code) {
        Optional<DepartmentDTO> departmentDtoOptional = departmentService.getByDepartmentCode(code);
        return departmentDtoOptional.map(departmentDTO -> new ResponseEntity<>(departmentDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
