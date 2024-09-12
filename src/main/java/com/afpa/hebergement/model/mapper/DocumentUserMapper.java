package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.AfpaCenterDTO;
import com.afpa.hebergement.model.dto.AppUserDTO;
import com.afpa.hebergement.model.dto.DocumentTypeDTO;
import com.afpa.hebergement.model.dto.DocumentUserDTO;
import com.afpa.hebergement.model.entity.AfpaCenter;
import com.afpa.hebergement.model.entity.AppUser;
import com.afpa.hebergement.model.entity.DocumentType;
import com.afpa.hebergement.model.entity.DocumentUser;

public class DocumentUserMapper {

    private DocumentUserMapper() {}

    public static DocumentUserDTO mapToDocumentUserDTO(DocumentUser documentUser) {

        AppUserDTO appUserDTO = AppUserMapper.mapToAppUserDTO(documentUser.getIdUser());
        AfpaCenterDTO afpaCenterDTO = AfpaCenterMapper.mapToAfpaCenterDto(documentUser.getIdAfpaCenter());
        DocumentTypeDTO documentTypeDTO = DocumentTypeMapper.mapToDocumentTypeDto(documentUser.getIdDocumentType());

        DocumentUserDTO documentUserDTO = new DocumentUserDTO();
        documentUserDTO.setId(documentUser.getId());
        documentUserDTO.setDocumentTitle(documentUser.getDocumentTitle());
        documentUserDTO.setDocumentCode(documentUser.getDocumentCode());
        documentUserDTO.setDateDepoDoc(documentUser.getDateDepoDoc());
        documentUserDTO.setIdUser(appUserDTO);
        documentUserDTO.setIdDocumentType(documentTypeDTO);
        documentUserDTO.setIdAfpaCenter(afpaCenterDTO);
        return documentUserDTO;
    }

    public static DocumentUser mapToDocumentUser(DocumentUserDTO documentUserDTO) {

        AppUser appUser = AppUserMapper.mapToAppUser(documentUserDTO.getIdUser());
        AfpaCenter afpaCenter = AfpaCenterMapper.mapToAfpaCenter(documentUserDTO.getIdAfpaCenter());
        DocumentType documentType = DocumentTypeMapper.mapToDocumentType(documentUserDTO.getIdDocumentType());

        DocumentUser documentUser = new DocumentUser();
        documentUser.setId(documentUserDTO.getId());
        documentUser.setDocumentTitle(documentUser.getDocumentTitle());
        documentUser.setDocumentCode(documentUser.getDocumentCode());
        documentUser.setDateDepoDoc(documentUser.getDateDepoDoc());
        documentUser.setIdUser(appUser);
        documentUser.setIdDocumentType(documentType);
        documentUser.setIdAfpaCenter(afpaCenter);
        return documentUser;
    }
}
