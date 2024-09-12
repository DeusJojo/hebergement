package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.RegionDTO;
import com.afpa.hebergement.model.entity.Region;

public class RegionMapper {

    private RegionMapper() {}

    public static RegionDTO mapToRegionDto(Region region) {
        RegionDTO regionDto = new RegionDTO();
        regionDto.setId(region.getId());
        regionDto.setRegionName(region.getRegionName().toUpperCase());
        return regionDto;
    }

    public static Region mapToRegion(RegionDTO regionDto) {
        Region region = new Region();
        region.setId(regionDto.getId());
        region.setRegionName(regionDto.getRegionName().toLowerCase().trim());
        return region;
    }
}
