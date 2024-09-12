package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.AfpaCenterDTO;
import com.afpa.hebergement.model.dto.CityDTO;
import com.afpa.hebergement.model.entity.AfpaCenter;
import com.afpa.hebergement.model.entity.City;
import com.afpa.hebergement.util.StringUtil;

public class AfpaCenterMapper {

    private AfpaCenterMapper() {}

    public static AfpaCenterDTO mapToAfpaCenterDto(AfpaCenter afpaCenter) {

        CityDTO cityDto = CityMapper.mapToCityDto(afpaCenter.getIdCity());

        AfpaCenterDTO afpaCenterDTO = new AfpaCenterDTO();
        afpaCenterDTO.setId(afpaCenter.getId());
        afpaCenterDTO.setCenterName(afpaCenter.getCenterName().toUpperCase());
        afpaCenterDTO.setAddressCenter(StringUtil.capitalize(afpaCenter.getAddressCenter()));

        if (afpaCenter.getComplementAddrCenter() != null){
            afpaCenterDTO.setComplementAddrCenter(StringUtil.capitalize(afpaCenter.getComplementAddrCenter()));
        } else {
            afpaCenterDTO.setComplementAddrCenter(null);
        }

        afpaCenterDTO.setSiren(afpaCenter.getSiren());
        afpaCenterDTO.setTvaNumber(afpaCenter.getTvaNumber().toUpperCase());
        afpaCenterDTO.setCenterPhoneNum(afpaCenter.getCenterPhoneNum());

        if (afpaCenter.getFaxCenter() != null){
            afpaCenterDTO.setFaxCenter(afpaCenter.getFaxCenter());
        } else {
            afpaCenterDTO.setFaxCenter(null);
        }

        afpaCenterDTO.setCodeCenter(afpaCenter.getCodeCenter());
        afpaCenterDTO.setCenterManager(afpaCenter.getCenterManager().toUpperCase());
        afpaCenterDTO.setIdCity(cityDto);
        return afpaCenterDTO;
    }

    public static AfpaCenter mapToAfpaCenter(AfpaCenterDTO afpaCenterDto) {

        City city = CityMapper.mapToCity(afpaCenterDto.getIdCity());

        AfpaCenter afpaCenter = new AfpaCenter();
        afpaCenter.setId(afpaCenterDto.getId());
        afpaCenter.setCenterName(afpaCenterDto.getCenterName().toLowerCase().trim());
        afpaCenter.setAddressCenter(afpaCenterDto.getAddressCenter().toLowerCase().trim());

        if(afpaCenterDto.getComplementAddrCenter() != null){
            afpaCenter.setComplementAddrCenter(afpaCenterDto.getComplementAddrCenter().toLowerCase().trim());

        }

        afpaCenter.setSiren(StringUtil.deleteSpace(afpaCenterDto.getSiren().trim()));
        afpaCenter.setTvaNumber(StringUtil.deleteSpace(afpaCenterDto.getTvaNumber().toLowerCase().trim()));
        afpaCenter.setCenterPhoneNum(StringUtil.deleteSpace(afpaCenterDto.getCenterPhoneNum().trim()));

        if (afpaCenterDto.getFaxCenter() != null){
            afpaCenter.setFaxCenter(StringUtil.deleteSpace(afpaCenterDto.getFaxCenter().trim()));
        }

        afpaCenter.setCodeCenter(afpaCenterDto.getCodeCenter().toLowerCase().trim());
        afpaCenter.setCenterManager(afpaCenterDto.getCenterManager().toLowerCase().trim());
        afpaCenter.setIdCity(city);
        return afpaCenter;
    }
}
