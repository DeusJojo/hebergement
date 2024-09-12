package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.CreationException;
import com.afpa.hebergement.exception.DuplicateException;
import com.afpa.hebergement.exception.InternalServerException;
import com.afpa.hebergement.exception.ResourceNotFoundException;
import com.afpa.hebergement.model.dto.CityDTO;
import com.afpa.hebergement.model.dto.DepartmentDTO;
import com.afpa.hebergement.model.dto.RegionDTO;
import com.afpa.hebergement.model.entity.City;
import com.afpa.hebergement.model.entity.Department;
import com.afpa.hebergement.model.entity.Region;
import com.afpa.hebergement.model.mapper.CityMapper;
import com.afpa.hebergement.model.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Classe de test pour {@link CityServiceImpl}.
 * Elle vérifie les différents cas d'utilisation et comportements attendus du service de gestion des villes.
 */
@ExtendWith(SpringExtension.class)
class CityAppServiceImplTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityServiceImpl cityService;

    private CityDTO cityDto;
    private City city;

    /**
     * Méthode de configuration exécutée avant chaque test.
     * Initialise les objets nécessaires pour les tests.
     */
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

        cityDto = new CityDTO(1, "lille", "59000", "59000", departmentDto);
        city = new City();
        city.setId(1);
        city.setCityName("lille");
        city.setInseeCode("59000");
        city.setPostcode("59000");
        city.setIdDepartment(department);
    }

    /**
     * Vérifie que la création d'une ville avec un DTO nul déclenche une {@link CreationException}.
     */
    @Test
    void testCreate_WhenCityDtoIsNull_ShouldThrowCreationException() {
        assertThrows(CreationException.class, () -> cityService.create(null));
    }

    /**
     * Vérifie que la création d'une ville existante déclenche une {@link DuplicateException}.
     */
    @Test
    void testCreate_WhenCityExists_ShouldThrowDuplicateException() {
        when(cityRepository.findByInseeCodeAndPostcode(cityDto.getInseeCode(), cityDto.getPostcode())).thenReturn(Optional.of(city));
        assertThrows(DuplicateException.class, () -> cityService.create(cityDto));
    }

    /**
     * Vérifie que la création d'une ville en cas de violation d'intégrité des données déclenche une {@link CreationException}.
     */
    @Test
    void testCreate_WhenDataIntegrityViolationExceptionThrown_ShouldThrowCreationException() {
        when(cityRepository.findByInseeCodeAndPostcode(cityDto.getInseeCode(), cityDto.getPostcode())).thenReturn(Optional.empty());
        when(cityRepository.save(any(City.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(CreationException.class, () -> cityService.create(cityDto));
    }

    /**
     * Vérifie que la création d'une ville en cas d'exception générique déclenche une {@link InternalServerException}.
     */
    @Test
    void testCreate_WhenExceptionThrown_ShouldThrowInternalServerException() {
        when(cityRepository.findByInseeCodeAndPostcode(cityDto.getInseeCode(), cityDto.getPostcode())).thenReturn(Optional.empty());
        when(cityRepository.save(any(City.class))).thenThrow(RuntimeException.class);

        assertThrows(InternalServerException.class, () -> cityService.create(cityDto));
    }

    /**
     * Vérifie que la création d'une ville valide retourne le DTO de la ville créée.
     */
    @Test
    void testCreate_WhenValid_ShouldReturnCityDTO() {
        when(cityRepository.findByInseeCodeAndPostcode(cityDto.getInseeCode(), cityDto.getPostcode())).thenReturn(Optional.empty());
        when(cityRepository.save(any(City.class))).thenReturn(city);

        CityDTO savedCityDto = cityService.create(cityDto);

        CityDTO expectedCityDto = new CityDTO(
                1,
                "lille",
                "59000",
                "59000",
                new DepartmentDTO(1, "nord", "59", new RegionDTO(1, "hauts-de-france"))
        );

        // Convert expected values to uppercase
        expectedCityDto.setCityName(expectedCityDto.getCityName().toUpperCase());
        expectedCityDto.getIdDepartment().setDepartmentName(expectedCityDto.getIdDepartment().getDepartmentName().toUpperCase());
        expectedCityDto.getIdDepartment().getIdRegion().setRegionName(expectedCityDto.getIdDepartment().getIdRegion().getRegionName().toUpperCase());

        assertThat(savedCityDto).usingRecursiveComparison().isEqualTo(expectedCityDto);
    }

    /**
     * Vérifie que la récupération d'une ville par ID, si elle existe, retourne le DTO de la ville.
     */
    @Test
    void testGetById_WhenCityExists_ShouldReturnCityDTO() {
        when(cityRepository.findById(city.getId())).thenReturn(Optional.of(city));

        Optional<CityDTO> cityDTO = cityService.getById(city.getId());

        assertTrue(cityDTO.isPresent());
        CityDTO expectedCityDto = CityMapper.mapToCityDto(city);
        assertThat(cityDTO.get()).usingRecursiveComparison().isEqualTo(expectedCityDto);
    }

    /**
     * Vérifie que la récupération d'une ville par ID, si elle n'existe pas, déclenche une {@link ResourceNotFoundException}.
     */
    @Test
    void testGetById_WhenCityDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(cityRepository.findById(city.getId())).thenReturn(Optional.empty());

        Integer cityId = city.getId();
        assertThrows(ResourceNotFoundException.class, () -> cityService.getById(cityId));
    }

    /**
     * Vérifie que la récupération d'une ville par code INSEE, si elle existe, retourne le DTO de la ville.
     */
    @Test
    void testGetByInseeCode_WhenCityExists_ShouldReturnCityDTO() {
        when(cityRepository.findByInseeCodeAndPostcode(city.getInseeCode(), cityDto.getPostcode())).thenReturn(Optional.of(city));

        Optional<CityDTO> cityDTO = cityService.getByInseeCodeAndPostCode(city.getInseeCode(), cityDto.getPostcode());

        assertTrue(cityDTO.isPresent());
        CityDTO expectedCityDto = CityMapper.mapToCityDto(city);
        assertThat(cityDTO.get()).usingRecursiveComparison().isEqualTo(expectedCityDto);
    }

    /**
     * Vérifie que la récupération d'une ville par code INSEE, si elle n'existe pas, déclenche une {@link ResourceNotFoundException}.
     */
    @Test
    void testGetByInseeCode_WhenCityDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(cityRepository.findByInseeCodeAndPostcode(city.getInseeCode(), city.getPostcode())).thenReturn(Optional.empty());

        String inseeCode = city.getInseeCode();
        String postCode = city.getPostcode();
        assertThrows(ResourceNotFoundException.class, () -> cityService.getByInseeCodeAndPostCode(inseeCode, postCode));
    }
}
