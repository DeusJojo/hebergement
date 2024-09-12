package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.AfpaCenterDTO;
import com.afpa.hebergement.model.dto.GuaranteeDTO;
import com.afpa.hebergement.model.dto.GuaranteeTypeDTO;
import com.afpa.hebergement.model.entity.AfpaCenter;
import com.afpa.hebergement.model.entity.Guarantee;
import com.afpa.hebergement.model.entity.GuaranteeType;
import com.afpa.hebergement.util.FormatUtil;

public class GuaranteeMapper {

    private GuaranteeMapper(){}

    public static GuaranteeDTO mapToGuaranteeDto(Guarantee guarantee){

        AfpaCenterDTO afpaCenterDto = AfpaCenterMapper.mapToAfpaCenterDto(guarantee.getIdAfpaCenter());
        GuaranteeTypeDTO guaranteeTypeDto = GuaranteeTypeMapper.mapToGuaranteeTypeDTO(guarantee.getIdGuaranteeType());

        GuaranteeDTO guaranteeDto = new GuaranteeDTO();
        guaranteeDto.setId(guarantee.getId());
        guaranteeDto.setAmount(FormatUtil.setAmount(guarantee.getAmount()));
        guaranteeDto.setIdAfpaCenter(afpaCenterDto);
        guaranteeDto.setIdGuaranteeType(guaranteeTypeDto);
        return guaranteeDto;
    }

    public static Guarantee mapToGuarantee(GuaranteeDTO guaranteeDto){

        AfpaCenter afpaCenter = AfpaCenterMapper.mapToAfpaCenter(guaranteeDto.getIdAfpaCenter());
        GuaranteeType guaranteeType = GuaranteeTypeMapper.mapToGuaranteeType(guaranteeDto.getIdGuaranteeType());

        Guarantee guarantee = new Guarantee();
        guarantee.setId(guarantee.getId());
        guarantee.setAmount(guaranteeDto.getAmount());
        guarantee.setIdAfpaCenter(afpaCenter);
        guarantee.setIdGuaranteeType(guaranteeType);
        return guarantee;
    }
}
