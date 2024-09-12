package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.DocumentCenter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentCenterRepository extends JpaRepository<DocumentCenter, Integer> {
    List<DocumentCenter> findAllByIdDocumentType_IdAndIdAfpaCenter_Id(
            Integer idDocumentType, Integer idAfpaCenter);
    List<DocumentCenter> findAllByIdAfpaCenter_Id(Integer idAfpaCenter);
    //rechercher tous les documents du centre afpa par centre l'id du centre avec pagination
    Page<DocumentCenter> findByIdAfpaCenter_Id(Integer afpaCenter, Pageable pageable);
}
