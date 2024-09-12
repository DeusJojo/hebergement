package com.afpa.hebergement.service.entity_service;

import com.afpa.hebergement.model.dto.DocumentCenterDTO;
import com.afpa.hebergement.service.GenericService;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface DocumentCenterService extends GenericService<DocumentCenterDTO, Integer> {
    DocumentCenterDTO uploadFile(MultipartFile multipartFile, Integer idAfpaCenter, Integer idDocumentType, String commentary);
    List<DocumentCenterDTO> getAllByDocumentTypeAndIdAfpaCenter(Integer idDocumentType, Integer idAfpaCenter);
    List<DocumentCenterDTO> getAllByAfpaCenterId(Integer idAfpaCenter);
    Optional<DocumentCenterDTO> updateFile(MultipartFile multipartFile, Integer idDocumentType, String commentary, Integer id);
    //récupérer les users par centre afpa avec pagination
    Page<DocumentCenterDTO> getAllByAfpaCenter(Integer idAfpaCenter, Integer page, Integer size);
}
