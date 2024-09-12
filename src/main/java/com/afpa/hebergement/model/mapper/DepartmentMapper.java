package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.DepartmentDTO;
import com.afpa.hebergement.model.dto.RegionDTO;
import com.afpa.hebergement.model.entity.Department;
import com.afpa.hebergement.model.entity.Region;

public class DepartmentMapper {

    private DepartmentMapper(){}

    public static DepartmentDTO mapToDepartmentDto(Department department){

        RegionDTO regionDto = RegionMapper.mapToRegionDto(department.getIdRegion());

        DepartmentDTO departmentDto = new DepartmentDTO();
        departmentDto.setId(department.getId());
        departmentDto.setDepartmentName(department.getDepartmentName().toUpperCase());
        departmentDto.setDepartmentCode(department.getDepartmentCode());
        departmentDto.setIdRegion(regionDto);
        return departmentDto;
    }

    public static Department mapToDepartment(DepartmentDTO departmentDto){

        Region region = RegionMapper.mapToRegion(departmentDto.getIdRegion());

        Department department = new Department();
        department.setId(departmentDto.getId());
        department.setDepartmentName(departmentDto.getDepartmentName().toLowerCase().trim());
        department.setDepartmentCode(departmentDto.getDepartmentCode().trim());
        department.setIdRegion(region);
        return department;
    }
}
