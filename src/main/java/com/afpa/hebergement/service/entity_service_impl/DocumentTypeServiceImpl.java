package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.CreationException;
import com.afpa.hebergement.exception.InternalServerException;
import com.afpa.hebergement.exception.ResourceNotFoundException;
import com.afpa.hebergement.model.dto.DocumentTypeDTO;
import com.afpa.hebergement.model.entity.DocumentType;
import com.afpa.hebergement.model.mapper.DocumentTypeMapper;
import com.afpa.hebergement.model.repository.DocumentTypeRepository;
import com.afpa.hebergement.service.entity_service.DocumentTypeService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service pour gérer les opérations sur les types de documents.
 */
@Service
@AllArgsConstructor
public class DocumentTypeServiceImpl implements DocumentTypeService {

    private final DocumentTypeRepository documentTypeRepository;

    private static final String AN_ERROR_OCCURRED = "An error occurred.";
    private static final String DOCUMENT_TYPE_DOES_NOT_EXIST = "Document type does not exist";

    /**
     * Crée un nouveau type de document à partir du DTO fourni.
     *
     * @param documentTypeDto Le DTO du type de document à créer
     * @return Le DTO du type de document créé
     */
    @Override
    public DocumentTypeDTO create(DocumentTypeDTO documentTypeDto) {
        if (documentTypeDto == null) throw new CreationException("Invalid document type provided.");

        try {
            DocumentType documentType = DocumentTypeMapper.mapToDocumentType(documentTypeDto);
            DocumentType savedDocumentType = documentTypeRepository.save(documentType);
            return DocumentTypeMapper.mapToDocumentTypeDto(savedDocumentType);
        } catch (DataIntegrityViolationException e) {
            throw new CreationException("Unable to create document type due to a data integrity issue.");
        } catch (Exception e) {
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }
    }

    /**
     * Récupère tous les types de documents.
     *
     * @return La liste des DTO de tous les types de documents
     */
    @Override
    public List<DocumentTypeDTO> getAll() {
        try {
            List<DocumentType> documentTypesList = documentTypeRepository.findAll();
            return documentTypesList.stream().map(DocumentTypeMapper::mapToDocumentTypeDto).toList();
        } catch (Exception e) {
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }
    }

    /**
     * Récupère un type de document par son ID.
     *
     * @param id L'ID du type de document à récupérer
     * @return Le DTO du type de document correspondant à l'ID ou une exception si non trouvé
     */
    @Override
    public Optional<DocumentTypeDTO> getById(Integer id) {
        DocumentType documentType = documentTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(DOCUMENT_TYPE_DOES_NOT_EXIST));
        return Optional.of(DocumentTypeMapper.mapToDocumentTypeDto(documentType));
    }

    /**
     * Récupère un type de document par son libellé (wording).
     *
     * @param wordingDocument Le libellé du type de document à récupérer
     * @return Le DTO du type de document correspondant au libellé ou une exception si non trouvé
     */
    @Override
    public Optional<DocumentTypeDTO> getByWordingDocument(String wordingDocument) {
        DocumentType documentType = documentTypeRepository.findByWordingDocumentType(wordingDocument.toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException(DOCUMENT_TYPE_DOES_NOT_EXIST));
        return Optional.of(DocumentTypeMapper.mapToDocumentTypeDto(documentType));
    }

    /**
     * Met à jour un type de document existant.
     *
     * @param id                L'ID du type de document à mettre à jour
     * @param documentTypeDto   Les nouvelles données pour le type de document
     * @return Le DTO du type de document mis à jour ou une exception si non trouvé
     */
    @Override
    public Optional<DocumentTypeDTO> update(Integer id, DocumentTypeDTO documentTypeDto) {
        DocumentType documentType = documentTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(DOCUMENT_TYPE_DOES_NOT_EXIST));

        try {
            documentType.setWordingDocumentType(documentTypeDto.getWordingDocumentType());
            DocumentType updatedDocumentType = documentTypeRepository.save(documentType);
            return Optional.of(DocumentTypeMapper.mapToDocumentTypeDto(updatedDocumentType));
        } catch (Exception e) {
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }
    }

    /**
     * Supprime un type de document par son ID.
     *
     * @param id L'ID du type de document à supprimer
     */
    @Override
    public void deleteById(Integer id) {
        DocumentType documentType = documentTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(DOCUMENT_TYPE_DOES_NOT_EXIST));
        try {
            documentTypeRepository.delete(documentType);
        } catch (Exception e) {
            throw new InternalServerException(AN_ERROR_OCCURRED);
        }
    }
}
