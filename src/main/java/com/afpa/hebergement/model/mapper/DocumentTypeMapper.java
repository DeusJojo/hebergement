package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.DocumentTypeDTO;
import com.afpa.hebergement.model.entity.DocumentType;

public class DocumentTypeMapper {

    private DocumentTypeMapper() {}

    public static DocumentTypeDTO mapToDocumentTypeDto(DocumentType documentType) {

        DocumentTypeDTO documentTypeDTO = new DocumentTypeDTO();
        documentTypeDTO.setId(documentType.getId());
        documentTypeDTO.setWordingDocumentType(documentType.getWordingDocumentType().toUpperCase());
        return documentTypeDTO;
    }

    public static DocumentType mapToDocumentType(DocumentTypeDTO documentTypeDTO) {
        DocumentType documentType = new DocumentType();
        documentType.setId(documentTypeDTO.getId());
        documentType.setWordingDocumentType(documentTypeDTO.getWordingDocumentType().toLowerCase().trim());
        return documentType;
    }
}
