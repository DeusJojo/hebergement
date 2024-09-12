package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, Integer> {
    Optional<DocumentType> findByWordingDocumentType(String wordingDocument);
}
