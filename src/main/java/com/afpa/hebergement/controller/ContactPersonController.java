package com.afpa.hebergement.controller;

import com.afpa.hebergement.model.dto.ContactPersonDTO;
import com.afpa.hebergement.service.entity_service.ContactPersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Contrôleur pour gérer les personnes à contacter.
 */
@Tag(name = "Contact Person Controller", description = "Endpoints to manage contact persons")
@RestController
@AllArgsConstructor
@RequestMapping("/api/contact-people")
public class ContactPersonController {

    private final ContactPersonService contactPersonService;

    /**
     * Crée une nouvelle personne à contacter.
     *
     * @param contactPersonDto Les informations de la personne à contacter.
     * @return ResponseEntity contenant la personne à contacter créée et le statut HTTP 201 (Created).
     */
    @Operation(
            description = "Create a contact person"
    )
    @PostMapping("/create")
    public ResponseEntity<ContactPersonDTO> create(@RequestBody ContactPersonDTO contactPersonDto) {
        ContactPersonDTO savedContactPerson = contactPersonService.create(contactPersonDto);
        return new ResponseEntity<>(savedContactPerson, HttpStatus.CREATED);
    }

    /**
     * Récupère une personne à contacter par son ID.
     *
     * @param id L'ID de la personne à contacter.
     * @return ResponseEntity contenant la personne à contacter si trouvée, sinon le statut HTTP 404 (Not Found).
     */
    @Operation(
            description = "Get a contact person by his id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ContactPersonDTO> getById(@PathVariable("id") Integer id) {
        Optional<ContactPersonDTO> contactPersonDto = contactPersonService.getById(id);
        return contactPersonDto.map(contactPerson -> new ResponseEntity<>(contactPerson, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Met à jour une personne à contacter existante.
     *
     * @param id L'ID de la personne à contacter à mettre à jour.
     * @param contactPersonDto Les nouvelles informations de la personne à contacter.
     * @return ResponseEntity contenant la personne à contacter mise à jour et le statut HTTP 200 (OK).
     */
    @Operation(
            description = "Update a contact person"
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<ContactPersonDTO> update(@PathVariable("id") Integer id,
                                                   @RequestBody ContactPersonDTO contactPersonDto) {
        Optional<ContactPersonDTO> updatedContactPersonDtoOptional = contactPersonService.update(id, contactPersonDto);
        return updatedContactPersonDtoOptional.map(updatedContactDtoPerson -> new ResponseEntity<>(updatedContactDtoPerson, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Supprime une personne à contacter par son ID.
     *
     * @param id L'ID de la personne à contacter à supprimer.
     * @return ResponseEntity contenant le statut HTTP 204 (No Content) si la suppression est réussie.
     */
    @Operation(
            description = "Delete a contact person"
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable("id") Integer id) {
        contactPersonService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
