package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.GuaranteeTypeDTO;
import com.afpa.hebergement.model.entity.GuaranteeType;

public class GuaranteeTypeMapper {

    private GuaranteeTypeMapper() {}

    public static GuaranteeTypeDTO mapToGuaranteeTypeDTO(GuaranteeType guaranteeType) {

        GuaranteeTypeDTO guaranteeTypeDTO = new GuaranteeTypeDTO();
        guaranteeTypeDTO.setId(guaranteeType.getId());
        guaranteeTypeDTO.setWordingGuaranteeType(guaranteeType.getWordingGuaranteeType().toUpperCase());
        return guaranteeTypeDTO;
    }

    public static GuaranteeType mapToGuaranteeType(GuaranteeTypeDTO guaranteeTypeDTO) {
        GuaranteeType guaranteeType = new GuaranteeType();
        guaranteeType.setId(guaranteeTypeDTO.getId());
        guaranteeType.setWordingGuaranteeType(guaranteeTypeDTO.getWordingGuaranteeType().toLowerCase().trim());
        return guaranteeType;
    }
}
