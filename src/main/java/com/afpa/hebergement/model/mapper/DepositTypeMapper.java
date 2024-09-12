package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.DepositTypeDTO;
import com.afpa.hebergement.model.entity.DepositType;

import java.util.stream.Collectors;

public class DepositTypeMapper {
    public static DepositTypeDTO mapToDTO(DepositType depositType) {
        DepositTypeDTO depositTypeDto = new DepositTypeDTO();
        depositTypeDto.setId(depositType.getId());
        depositTypeDto.setWordingDepositType(depositType.getWordingDepositType());
        depositTypeDto.setDepositsDTO(depositType.getDeposits().stream().map(DepositMapper::mapToDTO).collect(Collectors.toSet()));
        return depositTypeDto;
    }

    public static DepositType mapToEntity(DepositTypeDTO depositTypeDTO){
        DepositType depositType = new DepositType();
        depositType.setId(depositTypeDTO.getId());
        depositType.setWordingDepositType(depositTypeDTO.getWordingDepositType());
        depositType.setDeposits(depositTypeDTO.getDepositsDTO().stream().map(DepositMapper::mapToEntity).collect(Collectors.toSet()));
        return depositType;
    }
}
