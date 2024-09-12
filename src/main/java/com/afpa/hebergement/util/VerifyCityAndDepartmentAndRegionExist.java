package com.afpa.hebergement.util;

import com.afpa.hebergement.model.entity.City;
import com.afpa.hebergement.model.entity.Department;
import com.afpa.hebergement.model.entity.Region;
import com.afpa.hebergement.model.repository.CityRepository;
import com.afpa.hebergement.model.repository.DepartmentRepository;
import com.afpa.hebergement.model.repository.RegionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerifyCityAndDepartmentAndRegionExist {

    public City manageCity(City city, CityRepository cityRepository, DepartmentRepository departmentRepository, RegionRepository regionRepository) {
        Optional<City> existingCity = cityRepository.findByInseeCodeAndPostcode(city.getInseeCode(), city.getPostcode());
        return existingCity.orElseGet(() -> {
            Department department = manageDepartment(city.getIdDepartment(), departmentRepository, regionRepository);
            city.setIdDepartment(department);
            return cityRepository.save(city);
        });
    }

    private Department manageDepartment(Department department, DepartmentRepository departmentRepository, RegionRepository regionRepository) {
        Optional<Department> existingDepartment = departmentRepository.findByDepartmentCode(department.getDepartmentCode());
        return existingDepartment.orElseGet(() -> {
            Region region = manageRegion(department.getIdRegion(), regionRepository);
            department.setIdRegion(region);
            return departmentRepository.save(department);
        });
    }

    private Region manageRegion(Region region, RegionRepository regionRepository) {
        Optional<Region> existingRegion = regionRepository.findByRegionName(region.getRegionName());
        return existingRegion.orElseGet(() -> regionRepository.save(region));
    }
}
