package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.AppServiceDTO;
import com.afpa.hebergement.model.entity.AppService;
import com.afpa.hebergement.util.StringUtil;

import java.util.stream.Collectors;

public class AppServiceMapper {

    private AppServiceMapper() {}

    public static AppServiceDTO mapToServiceDto(AppService appService) {
        AppServiceDTO appServiceDto = new AppServiceDTO();
        appServiceDto.setId(appService.getId());
        appServiceDto.setWordingService(StringUtil.capitalizeFirstLetter(appService.getWordingService()));

        if(appService.getAfpaCenters() != null && !appService.getAfpaCenters().isEmpty()){
            appServiceDto.setAfpaCenters(appService.getAfpaCenters().stream()
                    .map(AfpaCenterMapper::mapToAfpaCenterDto)
                    .collect(Collectors.toSet()));
        }
        return appServiceDto;
    }

    public static AppService mapToService(AppServiceDTO appServiceDto) {
        AppService appService = new AppService();
        appService.setId(appServiceDto.getId());
        appService.setWordingService(appServiceDto.getWordingService().toLowerCase());

        if(appServiceDto.getAfpaCenters() != null && !appServiceDto.getAfpaCenters().isEmpty()){
            appService.setAfpaCenters(appServiceDto.getAfpaCenters().stream()
                    .map(AfpaCenterMapper::mapToAfpaCenter)
                    .collect(Collectors.toSet()));
        }

        return appService;
    }
}
