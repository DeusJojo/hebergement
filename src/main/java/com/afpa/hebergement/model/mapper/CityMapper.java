package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.CityDTO;
import com.afpa.hebergement.model.dto.DepartmentDTO;
import com.afpa.hebergement.model.entity.City;
import com.afpa.hebergement.model.entity.Department;

public class CityMapper {

    private CityMapper() {}

    public static CityDTO mapToCityDto(City city){

        DepartmentDTO departmentDto = DepartmentMapper.mapToDepartmentDto(city.getIdDepartment());

        CityDTO cityDto = new CityDTO();
        cityDto.setId(city.getId());
        cityDto.setCityName(city.getCityName().toUpperCase());
        cityDto.setPostcode(city.getPostcode());
        cityDto.setInseeCode(city.getInseeCode());
        cityDto.setIdDepartment(departmentDto);
        return cityDto;
    }

    public static City mapToCity(CityDTO cityDto){

        Department department = DepartmentMapper.mapToDepartment(cityDto.getIdDepartment());

        City city = new City();
        city.setId(cityDto.getId());
        city.setCityName(cityDto.getCityName().toLowerCase().trim());
        city.setInseeCode(cityDto.getInseeCode().trim());
        city.setPostcode(cityDto.getPostcode().trim());
        city.setIdDepartment(department);
        return city;
    }
}
