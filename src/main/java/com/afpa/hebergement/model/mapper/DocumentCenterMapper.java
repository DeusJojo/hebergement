package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.AfpaCenterDTO;
import com.afpa.hebergement.model.dto.DocumentCenterDTO;
import com.afpa.hebergement.model.dto.DocumentTypeDTO;
import com.afpa.hebergement.model.entity.AfpaCenter;
import com.afpa.hebergement.model.entity.DocumentCenter;
import com.afpa.hebergement.model.entity.DocumentType;

public class DocumentCenterMapper {

    private DocumentCenterMapper() {}

    public static DocumentCenterDTO mapToDocumentCenterDTO(DocumentCenter documentCenter) {

        AfpaCenterDTO afpaCenterDTO = AfpaCenterMapper.mapToAfpaCenterDto(documentCenter.getIdAfpaCenter());
        DocumentTypeDTO documentTypeDTO = DocumentTypeMapper.mapToDocumentTypeDto(documentCenter.getIdDocumentType());

        DocumentCenterDTO documentCenterDTO = new DocumentCenterDTO();
        documentCenterDTO.setId(documentCenter.getId());
        documentCenterDTO.setDocumentName(documentCenter.getDocumentName());
        documentCenterDTO.setDocumentCode(documentCenter.getDocumentCode());
        documentCenterDTO.setDateDepoDoc(documentCenter.getDateDepoDoc());
        if(documentCenter.getDocCommentary() != null){
            documentCenterDTO.setDocCommentary(documentCenter.getDocCommentary());
        }
        documentCenterDTO.setIdAfpaCenter(afpaCenterDTO);
        documentCenterDTO.setIdDocumentType(documentTypeDTO);
        return documentCenterDTO;
    }

    public static DocumentCenter mapToDocumentCenter(DocumentCenterDTO documentCenterDTO) {

        AfpaCenter afpaCenter = AfpaCenterMapper.mapToAfpaCenter(documentCenterDTO.getIdAfpaCenter());
        DocumentType documentType = DocumentTypeMapper.mapToDocumentType(documentCenterDTO.getIdDocumentType());

        DocumentCenter documentCenter = new DocumentCenter();
        documentCenter.setId(documentCenterDTO.getId());
        documentCenter.setDocumentName(documentCenterDTO.getDocumentName().toLowerCase().trim());
        documentCenter.setDocumentCode(documentCenterDTO.getDocumentCode().trim());
        documentCenter.setDateDepoDoc(documentCenterDTO.getDateDepoDoc());
        if(documentCenter.getDocCommentary() != null){
            documentCenter.setDocCommentary(documentCenterDTO.getDocCommentary());
        }
        documentCenter.setIdAfpaCenter(afpaCenter);
        documentCenter.setIdDocumentType(documentType);
        return documentCenter;
    }
}
