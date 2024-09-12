package com.afpa.hebergement.controller;

import com.afpa.hebergement.model.dto.RoleDTO;
import com.afpa.hebergement.service.entity_service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Role", description = "Endpoints to manage roles")
@RestController
@RequestMapping("/api/roles")
@AllArgsConstructor
public class RoleController {


    private RoleService roleService;


    // Endpoint pour créer un nouveau rôle
    @PostMapping("/create")
    @Operation(summary = "Create new role")
    public ResponseEntity<RoleDTO> createRole(@Valid @RequestBody RoleDTO roleDto) {
        // Appel du service pour créer un nouveau rôle à partir du DTO
        RoleDTO createdRole = roleService.create(roleDto);
        // Retourne le rôle créé avec un statut HTTP 201 (Created)
        return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
    }


    // Endpoint pour récupérer tous les rôles
    @GetMapping
    @Operation(summary = "Get all roles")
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        // Appel du service pour obtenir tous les rôles
        List<RoleDTO> roles = roleService.getAll();
        // Retourne la liste des rôles avec un statut HTTP 200 (OK)
        return ResponseEntity.ok(roles);
    }


    // Endpoint pour récupérer un rôle par son ID
    @GetMapping("/{id}")
    @Operation(summary = "Get role by ID")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable("id") Integer idRole) {
        // Appel du service pour obtenir un rôle par son ID
        Optional<RoleDTO> role = roleService.getById(idRole);

        // Retourne le rôle avec un statut HTTP 200 (OK) si présent
        return role.map(ResponseEntity::ok)
                // Retourne une réponse avec un statut HTTP 404 (Not Found) si le role n'est pas trouvé
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



    // Endpoint pour mettre à jour un rôle par son ID
    @PutMapping("/update/{id}")
    @Operation(summary = "Update role by ID")
    public ResponseEntity<RoleDTO> updateRole(@PathVariable("id") Integer idRole, @Valid @RequestBody RoleDTO roleDto) {
        // Appel du service pour mettre à jour le rôle existant avec les nouvelles données
        Optional<RoleDTO> updatedRole = roleService.update(idRole, roleDto);

        // Retourne le rôle mis à jour avec un statut HTTP 200 (OK) si présent
        return updatedRole.map(ResponseEntity::ok)
                // Retourne une réponse avec un statut HTTP 404 (Not Found) si le role n'est pas trouvé
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



    // Endpoint pour supprimer un rôle par son ID
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete role by ID")
    public ResponseEntity<HttpStatus> deleteRole(@PathVariable("id") Integer idRole) {
        // Appel du service pour supprimer le rôle par son ID
        roleService.deleteById(idRole);
        // Retourne un statut HTTP 204 (No Content) après suppression
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
