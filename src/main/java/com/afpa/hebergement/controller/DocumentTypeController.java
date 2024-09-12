package com.afpa.hebergement.controller;

import com.afpa.hebergement.model.dto.DocumentTypeDTO;
import com.afpa.hebergement.service.entity_service.DocumentTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Contrôleur pour gérer les types de documents.
 */
@Tag(name = "Document type Controller", description = "Endpoints to manage documents type")
@RestController
@AllArgsConstructor
@RequestMapping("/api/documents-type")
public class DocumentTypeController {

    private final DocumentTypeService documentTypeService;

    /**
     * Crée un nouveau type de document à partir du DTO fourni.
     *
     * @param documentTypeDto Le DTO du type de document à créer
     * @return Le DTO du type de document créé
     */
    @Operation(
            description = "Create a document type"
    )
    @PostMapping("/create")
    public ResponseEntity<DocumentTypeDTO> create(@RequestBody DocumentTypeDTO documentTypeDto) {
        DocumentTypeDTO savedDocumentType = documentTypeService.create(documentTypeDto);
        return new ResponseEntity<>(savedDocumentType, HttpStatus.CREATED);
    }

    /**
     * Récupère tous les types de documents.
     *
     * @return La liste des DTO de tous les types de documents
     */
    @Operation(
            description = "Get all documents type"
    )
    @GetMapping
    public ResponseEntity<List<DocumentTypeDTO>> getAll() {
        List<DocumentTypeDTO> documentTypeDtoList = documentTypeService.getAll();
        return new ResponseEntity<>(documentTypeDtoList, HttpStatus.OK);
    }

    /**
     * Récupère un type de document par son ID.
     *
     * @param id L'ID du type de document à récupérer
     * @return Le DTO du type de document correspondant à l'ID ou une réponse avec le statut HTTP 404 (Not Found) si non trouvé
     */
    @Operation(
            description = "Get a document type by his id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<DocumentTypeDTO> getById(@PathVariable("id") Integer id) {
        Optional<DocumentTypeDTO> documentTypeDto = documentTypeService.getById(id);
        return documentTypeDto.map(documentType -> new ResponseEntity<>(documentType, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Récupère un type de document par son libellé (wording).
     *
     * @param wordingDocumentType Le libellé du type de document à récupérer
     * @return Le DTO du type de document correspondant au libellé ou une réponse avec le statut HTTP 404 (Not Found) si non trouvé
     */
    @Operation(
            description = "Get a document type by his wording"
    )
    @GetMapping("/getByWording/{wording}")
    public ResponseEntity<DocumentTypeDTO> getByName(@PathVariable("wording") String wordingDocumentType) {
        Optional<DocumentTypeDTO> documentTypeDto = documentTypeService.getByWordingDocument(wordingDocumentType);
        return documentTypeDto.map(documentType -> new ResponseEntity<>(documentType, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Met à jour un type de document existant.
     *
     * @param id L'ID du type de document à mettre à jour
     * @param documentTypeDto Les nouvelles données pour le type de document
     * @return Le DTO du type de document mis à jour, ou une réponse avec le statut HTTP 404 (Not Found) si non trouvé
     */
    @Operation(
            description = "Update a document type"
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<DocumentTypeDTO> update(@PathVariable("id") Integer id,
                                                  @RequestBody DocumentTypeDTO documentTypeDto) {
        Optional<DocumentTypeDTO> updatedDocumentTypeDtoOptional = documentTypeService.update(id, documentTypeDto);
        return updatedDocumentTypeDtoOptional.map(updatedDocumentTypeDto -> new ResponseEntity<>(updatedDocumentTypeDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Supprime un type de document par son ID.
     *
     * @param id L'ID du type de document à supprimer
     * @return Une réponse avec le statut HTTP 204 (No Content) après suppression
     */
    @Operation(
            description = "Delete a document type"
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable("id") Integer id) {
        documentTypeService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
