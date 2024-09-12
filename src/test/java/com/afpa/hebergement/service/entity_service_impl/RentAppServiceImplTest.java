package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.CreationException;
import com.afpa.hebergement.model.dto.*;
import com.afpa.hebergement.model.entity.*;
import com.afpa.hebergement.model.mapper.AfpaCenterMapper;
import com.afpa.hebergement.model.repository.RentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
class RentAppServiceImplTest {

    @Mock
    private RentRepository rentRepository;

    @InjectMocks
    private RentServiceImpl rentService;

    @BeforeEach
    void setUp() {
        RegionDTO regionDto = new RegionDTO(1, "hauts-de-france");
        Region region = new Region();
        region.setId(1);
        region.setRegionName("hauts-de-france");

        DepartmentDTO departmentDto = new DepartmentDTO(1, "nord", "59", regionDto);
        Department department = new Department();
        department.setId(1);
        department.setDepartmentName("nord");
        department.setDepartmentCode("59");
        department.setIdRegion(region);

        CityDTO cityDto = new CityDTO(1, "lille", "59000", "59000", departmentDto);
        City city = new City();
        city.setId(1);
        city.setCityName("lille");
        city.setInseeCode("59000");
        city.setPostcode("59000");
        city.setIdDepartment(department);

        AfpaCenterDTO afpaCenterDTO = new AfpaCenterDTO();
        afpaCenterDTO.setId(2);
        afpaCenterDTO.setIdCity(cityDto);
        afpaCenterDTO.setCenterName("afpa roubaix");
        afpaCenterDTO.setAddressCenter("20 Rue du Luxembourg");
        afpaCenterDTO.setCenterPhoneNum("0972723936");
        afpaCenterDTO.setSiren("824363436");
        afpaCenterDTO.setTvaNumber("FR48824363436");
        afpaCenterDTO.setIdCity(cityDto);

        RentDTO rentDTO = new RentDTO(1, "jour", new BigDecimal(15), afpaCenterDTO);
        Rent rent = new Rent();
        rent.setId(1);
        rent.setFrequency("jour");
        rent.setAmount(new BigDecimal(30));
        rent.setIdAfpaCenter(AfpaCenterMapper.mapToAfpaCenter(afpaCenterDTO));
    }

    @Test
    void testCreate_WhenRentDtoIsNull_ShouldThrowCreationException() {
        assertThrows(CreationException.class, () -> rentService.create(null));
    }
  
}