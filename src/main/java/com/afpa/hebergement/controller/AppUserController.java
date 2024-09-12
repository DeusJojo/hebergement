package com.afpa.hebergement.controller;

import com.afpa.hebergement.model.dto.AppUserDTO;
import com.afpa.hebergement.model.dto.PasswordDTO;
import com.afpa.hebergement.model.dto.UserDTO;
import com.afpa.hebergement.service.entity_service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "AppUser", description = "Endpoints to manage App Users")
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Validated
public class

AppUserController {

    // Injection du service AppUserService
    private final AppUserService appUserService;


    // Endpoint pour créer un nouvel utilisateur
    @PostMapping("/create")
    @Operation(summary = "Create new AppUser")
    public ResponseEntity<AppUserDTO> createAppUser(@Valid @RequestBody AppUserDTO appUserDTO) {
        // Appel du service pour créer un nouvel utilisateur à partir du DTO
        AppUserDTO createdAppUser = appUserService.create(appUserDTO);
        // Retourne l'utilisateur créé avec un statut HTTP 201 (Created)
        return new ResponseEntity<>(createdAppUser, HttpStatus.CREATED);
    }

    // Endpoint pour récupérer tous les utilisateurs
    @GetMapping
    @Operation(summary = "Get all AppUsers")
//    @PreAuthorize("hasRole('INTENDANT')")
    public ResponseEntity<List<AppUserDTO>> getAllAppUsers() {
        // Appel du service pour obtenir tous les utilisateurs
        List<AppUserDTO> appUsers = appUserService.getAll();
        // Retourne la liste des utilisateurs avec un statut HTTP 200 (OK)
        return ResponseEntity.ok(appUsers);
    }


    // Endpoint pour récupérer les users avec pagination
    @GetMapping("/page")
    @Operation(summary = "Get users with pagination")
    public ResponseEntity<Page<AppUserDTO>> getUsersByPage(
            // Définit un paramètre de requête "page" avec une valeur par défaut de 0 (1ère page).
            @RequestParam(defaultValue = "0") int page,
            // Définit un paramètre de requête "size" avec une valeur par défaut de 10 (10 élements par page).
            @RequestParam(defaultValue = "10") int size) {

        // Appel du service pour obtenir les users avec pagination en utilisant les paramètres de page et de taille.
        Page<AppUserDTO> usersPage = appUserService.getAll(page, size);

        // Retourne la page des users avec un statut HTTP 200 (OK)
        return ResponseEntity.ok(usersPage);
    }


    // Endpoint pour récupérer toutes les users par centre Afpa
    @GetMapping("/afpa-center/{idAfpaCenter}")
    @Operation(summary = "Get all user by Afpa Center")
    public ResponseEntity<List<AppUserDTO>> getAllByAfpaCenter(@PathVariable("idAfpaCenter") Integer idAfpaCenter) {
        // Appel du service pour obtenir tous les utilisateurs
        List<AppUserDTO> users = appUserService.getAllByAfpaCenter(idAfpaCenter);
        // Retourne la liste des utilisateurs avec un statut HTTP 200 (OK)
        return ResponseEntity.ok(users);
    }


    // Endpoint pour récupérer tous les utilisateurs par centre Afpa avec pagination
    @GetMapping("/afpa-center/{idAfpaCenter}/page")
    @Operation(summary = "Get users by Afpa Center with pagination")
    public ResponseEntity<Page<AppUserDTO>> getUsersByAfpaCenterPage(
            @PathVariable("idAfpaCenter") Integer idAfpaCenter,
            // Définit un paramètre de requête "page" avec une valeur par défaut de 0 (1ère page)
            @RequestParam(defaultValue = "0") int page,
            // Définit un paramètre de requête "size" avec une valeur par défaut de 10 (10 éléments par page)
            @RequestParam(defaultValue = "10") int size) {

        // Appel du service pour obtenir les utilisateurs par centre Afpa avec pagination
        Page<AppUserDTO> usersPage = appUserService.getAllByAfpaCenter(idAfpaCenter, page, size);

        // Retourne la page des utilisateurs avec un statut HTTP 200 (OK)
        return ResponseEntity.ok(usersPage);
    }


    // Endpoint pour récupérer un utilisateur par son ID
    @GetMapping("/{id}")
    @Operation(summary = "Get AppUser by ID")
    public ResponseEntity<AppUserDTO> getAppUserById(@PathVariable("id") Integer id) {
        // Appel du service pour obtenir un utilisateur par son ID
        Optional<AppUserDTO> appUser = appUserService.getById(id);
        // Vérifie si l'utilisateur est présent et retourne l'utilisateur avec un statut HTTP 200 (OK)
        return appUser.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    // Endpoint pour récupérer un utilisateur par son numéro de bénéficiaire
    @GetMapping("/beneficiaryNumber/{beneficiaryNumber}")
    @Operation(summary = "Get AppUser by beneficiary number")
    public ResponseEntity<AppUserDTO> getAppUserByBeneficiaryNumber(@PathVariable("beneficiaryNumber") String beneficiaryNumber) {
        // Appel du service pour obtenir un utilisateur par son numéro de bénéficiaire
        Optional<AppUserDTO> appUser = appUserService.getByBeneficiaryNumber(beneficiaryNumber);
        // Vérifie si l'utilisateur est présent et retourne l'utilisateur avec un statut HTTP 200 (OK)
        return appUser.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    // Endpoint pour récupérer un utilisateur par son email
    @GetMapping("/email/{email}")
    @Operation(summary = "Get AppUser by email")
    public ResponseEntity<AppUserDTO> getAppUserByEmail(@PathVariable("email") String email) {
        // Appel du service pour obtenir un utilisateur par son email
        Optional<AppUserDTO> appUser = appUserService.getByEmail(email);
        // Vérifie si l'utilisateur est présent et retourne l'utilisateur avec un statut HTTP 200 (OK)
        return appUser.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    // Endpoint pour mettre à jour un utilisateur par son ID (admin)
    @PutMapping("/update/{id}")
    @Operation(summary = "Update AppUser by ID and admin")
    public ResponseEntity<AppUserDTO> updateAppUser(@PathVariable("id") Integer id, @Valid @RequestBody AppUserDTO appUserDTO) {
        // Appel du service pour mettre à jour l'utilisateur existant avec les nouvelles données
        Optional<AppUserDTO> updatedAppUser = appUserService.update(id, appUserDTO);
        // Vérifie si l'utilisateur mis à jour est présent et retourne l'utilisateur mis à jour avec un statut HTTP 200 (OK)
        return updatedAppUser.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    // Endpoint pour mettre à jour un utilisateur par son ID (user)
    @PutMapping("/update/guest/{id}")
    @Operation(summary = "Update AppUser by ID and by guest")
    public ResponseEntity<UserDTO> updateUserByGuest(@PathVariable("id") Integer id, @Valid @RequestBody UserDTO userDto) {
        // Appel du service pour mettre à jour l'utilisateur existant avec les nouvelles données
        Optional<UserDTO> updatedUser = appUserService.updateUser(id, userDto);
        // Vérifie si l'utilisateur mis à jour est présent et retourne l'utilisateur mis à jour avec un statut HTTP 200 (OK)
        return updatedUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Endpoint pour mettre à jour le mot de passe d'un utilisateur par son ID
    @PutMapping("/update/password/{id}")
    @Operation(summary = "Update password by user ID")
    public ResponseEntity<String> updatePassword(@PathVariable("id") Integer id, @Valid @RequestBody PasswordDTO passwordDto) {
        // Appel du service pour modifier le mdp
        appUserService.updatePassword(id, passwordDto);
        // Retourne un statut HTTP 200 (OK) avec message
        return ResponseEntity.ok("Password updated successfully");
    }


    // Endpoint pour supprimer un utilisateur par son ID
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete AppUser by ID")
    public ResponseEntity<HttpStatus> deleteAppUser(@PathVariable("id") Integer id) {
        // Appel du service pour supprimer un utilisateur par son ID
        appUserService.deleteById(id);
        // Retourne un statut HTTP 204 (No Content) après suppression
        return ResponseEntity.noContent().build();
    }

}

