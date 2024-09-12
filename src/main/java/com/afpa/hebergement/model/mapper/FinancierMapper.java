package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.FinancierDTO;
import com.afpa.hebergement.model.entity.Financier;

public class FinancierMapper {
    public static FinancierDTO mapToFinancierDTO(Financier financier){
        FinancierDTO financierDTO = new FinancierDTO();
        financierDTO.setId(financier.getId());
        financierDTO.setCoveringCost(financier.getCoveringCost());
        financierDTO.setLogName(financier.getLogName());
        financierDTO.setUrlLogoFinancier(financier.getUrlLogoFinancier());
        financierDTO.setParticipates(financier.getParticipates());
        return financierDTO;
    }

    public static Financier mapToFinancierEntity(FinancierDTO financierDTO){
        Financier financier = new Financier();
        financier.setId(financierDTO.getId());
        financier.setCoveringCost(financierDTO.getCoveringCost());
        financier.setLogName(financierDTO.getLogName());
        financier.setUrlLogoFinancier(financierDTO.getUrlLogoFinancier());
        financier.setParticipates(financierDTO.getParticipates());
        return financier;
    }
}
