package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.DocumentUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentUserRepository extends JpaRepository<DocumentUser, Integer> {
    List<DocumentUser> findAllByIdDocumentType_WordingDocumentType(String wordingDocumentType);
    List<DocumentUser> findAllByIdUser_Id(Integer idAfpaCenter);
}
