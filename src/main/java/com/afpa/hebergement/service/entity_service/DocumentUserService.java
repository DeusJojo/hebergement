package com.afpa.hebergement.service.entity_service;

import com.afpa.hebergement.model.dto.DocumentCenterDTO;
import com.afpa.hebergement.model.entity.DocumentUser;
import com.afpa.hebergement.service.GenericService;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentUserService extends GenericService<DocumentUser, Integer> {
    DocumentCenterDTO uploadFile(MultipartFile multipartFile, Integer idAfpaCenter, Integer idDocumentType, String commentary);
}
