package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.model.dto.DocumentCenterDTO;
import com.afpa.hebergement.model.entity.DocumentUser;
import com.afpa.hebergement.service.entity_service.DocumentUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DocumentUserServiceImpl implements DocumentUserService {
    @Override
    public Optional<DocumentUser> getById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public DocumentCenterDTO uploadFile(MultipartFile multipartFile, Integer idAfpaCenter, Integer idDocumentType, String commentary) {
        return null;
    }
}
