package com.afpa.hebergement.service.entity_service;

import com.afpa.hebergement.model.dto.DocumentTypeDTO;
import com.afpa.hebergement.service.GenericService;

import java.util.Optional;

public interface DocumentTypeService extends GenericService<DocumentTypeDTO, Integer> {
    Optional<DocumentTypeDTO> getByWordingDocument(String wordingDocument);
}
