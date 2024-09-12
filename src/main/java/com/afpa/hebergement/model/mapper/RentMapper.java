package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.AfpaCenterDTO;
import com.afpa.hebergement.model.dto.RentDTO;
import com.afpa.hebergement.model.entity.AfpaCenter;
import com.afpa.hebergement.model.entity.Rent;
import com.afpa.hebergement.util.FormatUtil;
import com.afpa.hebergement.util.StringUtil;

public class RentMapper {

    private RentMapper() {}

    public static RentDTO mapToRentDto(Rent rent){

        AfpaCenterDTO afpaCenterDto = AfpaCenterMapper.mapToAfpaCenterDto(rent.getIdAfpaCenter());

        RentDTO rentDto = new RentDTO();
        rentDto.setId(rent.getId());
        rentDto.setFrequency(StringUtil.capitalize(rent.getFrequency()));
        rentDto.setAmount(FormatUtil.setAmount(rent.getAmount()));
        rentDto.setIdAfpaCenter(afpaCenterDto);
        return rentDto;
    }

    public static Rent mapToRent(RentDTO rentDto){

        AfpaCenter afpaCenter = AfpaCenterMapper.mapToAfpaCenter(rentDto.getIdAfpaCenter());

        Rent rent = new Rent();
        rent.setId(rentDto.getId());
        rent.setFrequency(rentDto.getFrequency().toLowerCase().trim());
        rent.setAmount(rentDto.getAmount());
        rent.setIdAfpaCenter(afpaCenter);
        return rent;
    }
}
