package com.afpa.hebergement.controller;

import com.afpa.hebergement.exception.InternalServerException;
import com.afpa.hebergement.model.dto.DocumentCenterDTO;
import com.afpa.hebergement.service.entity_service.DocumentCenterService;
import com.afpa.hebergement.util.FileUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Contrôleur pour gérer les opérations liées aux documents du centre.
 */
@Tag(name = "Document center Controller", description = "Endpoints to manage document center")
@RestController
@AllArgsConstructor
@RequestMapping("/api/documents-center")
public class DocumentCenterController {

    private final DocumentCenterService documentCenterService;

    /**
     * Endpoint pour télécharger un fichier et retourner les informations du document du centre.
     *
     * @param idAfpaCenter   L'identifiant du centre Afpa
     * @param idDocumentType L'identifiant du type de document
     * @param multipartFile  Le fichier à télécharger
     * @param commentary     Le commentaire associé au document (optionnel)
     * @return Les informations du document du centre en DTO
     */
    @Operation(description = "Uploads a file and returns the document center information")
    @PostMapping("/uploadFile/{idAfpaCenter}/{idDocumentType}")
    public ResponseEntity<DocumentCenterDTO> uploadFile(
            @PathVariable("idAfpaCenter") Integer idAfpaCenter,
            @PathVariable("idDocumentType") Integer idDocumentType,
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam(value = "commentary", required = false) String commentary) {
        DocumentCenterDTO documentCenterDTO = documentCenterService.uploadFile(multipartFile, idAfpaCenter, idDocumentType, commentary);
        return new ResponseEntity<>(documentCenterDTO, HttpStatus.OK);
    }

    /**
     * Endpoint pour mettre à jour un fichier existant et retourner les informations du document du centre.
     *
     * @param idDocumentType L'identifiant du type de document
     * @param multipartFile  Le fichier à télécharger
     * @param commentary     Le commentaire associé au document (optionnel)
     * @param id             L'identifiant du document à mettre à jour
     * @return Les informations mises à jour du document du centre en DTO
     */
    @Operation(description = "Uploads a file and returns the document center information")
    @PutMapping("/updateFile/{idDocumentType}/{id}")
    public ResponseEntity<DocumentCenterDTO> updateFile(
            @PathVariable("idDocumentType") Integer idDocumentType,
            @RequestParam(value = "file", required = false) MultipartFile multipartFile,
            @RequestParam(value = "commentary", required = false) String commentary,
            @PathVariable("id") Integer id) {
        Optional<DocumentCenterDTO> updatedDocumentCenterDtoOptional = documentCenterService.updateFile(multipartFile, idDocumentType, commentary, id);
        return updatedDocumentCenterDtoOptional.map(updatedDocumentCenterDto -> new ResponseEntity<>(updatedDocumentCenterDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint pour télécharger un fichier à partir de son identifiant.
     *
     * @param id L'identifiant du document
     * @return Le fichier en tant que ressource et les informations de réponse appropriées
     */
    @Operation(description = "Download a file and returns the document center information")
    @GetMapping("/downloadFile/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") Integer id) {
        Resource resource = null;
        String fileCode = null;
        try {
            Optional<DocumentCenterDTO> documentCenterDto = documentCenterService.getById(id);
            if (documentCenterDto.isPresent()) {
                fileCode = documentCenterDto.get().getDocumentCode();
            } else {
                throw new InternalServerException("An error occurred.");
            }
            // Obtient le fichier en tant que ressource à partir du code du fichier
            resource = FileUtil.getFileAsResource(fileCode);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

        // Retourne le fichier avec les informations appropriées pour le téléchargement
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }

    @Operation(description = "Get all documents by pagination")
    @GetMapping("/documents/{id}/page")
    public ResponseEntity<Page<DocumentCenterDTO>> getAllByAfpaCenter(
            @PathVariable("id") Integer idAfpaCenter,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<DocumentCenterDTO> documentCenterDTOPage = documentCenterService.getAllByAfpaCenter(
                idAfpaCenter, page, size);
        return new ResponseEntity<>(documentCenterDTOPage, HttpStatus.OK);
    }


    /**
     * Endpoint pour obtenir tous les documents du centre par type de document.
     *
     * @param idDocumentType L'id du type de document
     * @param idAfpaCenter L'id du centre afpa
     * @return Une liste de tous les documents du centre pour le type et le centre afpa spécifié en DTO
     */
    @Operation(description = "Get all documents center by type")
    @GetMapping("/getAllByDocumentTypeAndIdAfpaCenter/{idType}/{idAfpa}")
    public ResponseEntity<List<DocumentCenterDTO>> getAllByDocumentTypeAndAfpaCenter(
            @PathVariable("idType") Integer idDocumentType,
            @PathVariable("idAfpa") Integer idAfpaCenter) {
        List<DocumentCenterDTO> documentCenterDtoList = documentCenterService
                .getAllByDocumentTypeAndIdAfpaCenter(idDocumentType, idAfpaCenter);
        return new ResponseEntity<>(documentCenterDtoList, HttpStatus.OK);
    }

    /**
     * Récupère tous les documents associés à un centre AFPA donné par son identifiant.
     *
     * @param idAfpaCenter L'identifiant du centre AFPA pour lequel les documents doivent être récupérés.
     * @return Une liste de tous les documents du centre pour le centre afpa spécifié en DTO
     */
    @Operation(description = "Get all documents center by afpa center id")
    @GetMapping("/getAllByIdAfpaCenter/{id}")
    public ResponseEntity<List<DocumentCenterDTO>> getAllByAfpaCenterId(@PathVariable("id") Integer idAfpaCenter) {
        List<DocumentCenterDTO> documentCenterDtoList = documentCenterService.getAllByAfpaCenterId(idAfpaCenter);
        return new ResponseEntity<>(documentCenterDtoList, HttpStatus.OK);
    }

    /**
     * Endpoint pour obtenir un document du centre par son identifiant.
     *
     * @param id L'identifiant du document
     * @return Les informations du document en DTO ou une réponse 404 NOT FOUND si le document n'existe pas
     */
    @Operation(description = "Get a document center by id")
    @GetMapping("/{id}")
    public ResponseEntity<DocumentCenterDTO> getById(@PathVariable("id") Integer id) {
        Optional<DocumentCenterDTO> documentCenterDtoOptional = documentCenterService.getById(id);
        return documentCenterDtoOptional.map(documentCenterDto -> new ResponseEntity<>(documentCenterDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint pour supprimer un document du centre par son identifiant.
     *
     * @param id L'identifiant du document à supprimer
     * @return Une réponse avec le code HTTP 204 NO CONTENT après la suppression
     */
    @Operation(description = "Delete a file")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable("id") Integer id){
        documentCenterService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
