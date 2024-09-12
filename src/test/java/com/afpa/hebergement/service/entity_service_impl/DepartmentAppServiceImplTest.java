package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.*;
import com.afpa.hebergement.model.dto.DepartmentDTO;
import com.afpa.hebergement.model.dto.RegionDTO;
import com.afpa.hebergement.model.entity.Department;
import com.afpa.hebergement.model.entity.Region;
import com.afpa.hebergement.model.mapper.DepartmentMapper;
import com.afpa.hebergement.model.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Classe de test pour {@link DepartmentServiceImpl}.
 * Elle vérifie les différents cas d'utilisation et comportements attendus du service de gestion des départements.
 */
@ExtendWith(SpringExtension.class)
class DepartmentAppServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private DepartmentDTO departmentDto;
    private Department department;

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

        departmentDto = new DepartmentDTO(1, "nord", "59", regionDto);
        department = new Department();
        department.setId(1);
        department.setDepartmentName("nord");
        department.setDepartmentCode("59");
        department.setIdRegion(region);
    }

    /**
     * Vérifie que la création d'un département avec un DTO nul déclenche une {@link CreationException}.
     */
    @Test
    void testCreate_WhenDepartmentDtoIsNull_ShouldThrowCreationException() {
        assertThrows(CreationException.class, () -> departmentService.create(null));
    }

    /**
     * Vérifie que la création d'un département existant déclenche une {@link DuplicateException}.
     */
    @Test
    void testCreate_WhenDepartmentExists_ShouldThrowDuplicateException() {
        when(departmentRepository.findByDepartmentCode(departmentDto.getDepartmentCode())).thenReturn(Optional.of(department));
        assertThrows(DuplicateException.class, () -> departmentService.create(departmentDto));
    }

    /**
     * Vérifie que la création d'un département en cas de violation d'intégrité des données déclenche une {@link CreationException}.
     */
    @Test
    void testCreate_WhenDataIntegrityViolationExceptionThrown_ShouldThrowCreationException() {
        when(departmentRepository.findByDepartmentCode(departmentDto.getDepartmentCode())).thenReturn(Optional.empty());
        when(departmentRepository.save(any(Department.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(CreationException.class, () -> departmentService.create(departmentDto));
    }

    /**
     * Vérifie que la création d'un département en cas d'exception générique déclenche une {@link InternalServerException}.
     */
    @Test
    void testCreate_WhenExceptionThrown_ShouldThrowInternalServerException() {
        when(departmentRepository.findByDepartmentCode(departmentDto.getDepartmentCode())).thenReturn(Optional.empty());
        when(departmentRepository.save(any(Department.class))).thenThrow(RuntimeException.class);

        assertThrows(InternalServerException.class, () -> departmentService.create(departmentDto));
    }

    /**
     * Vérifie que la création d'un département valide retourne le DTO du département créé.
     */
    @Test
    void testCreate_WhenValid_ShouldReturnDepartmentDTO() {
        when(departmentRepository.findByDepartmentCode(departmentDto.getDepartmentCode())).thenReturn(Optional.empty());
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        DepartmentDTO savedDepartmentDto = departmentService.create(departmentDto);

        assertNotNull(savedDepartmentDto);
        assertEquals(departmentDto.getDepartmentCode(), savedDepartmentDto.getDepartmentCode());
        assertEquals(departmentDto.getDepartmentName().toUpperCase(), savedDepartmentDto.getDepartmentName());
        assertEquals(departmentDto.getIdRegion().getId(), savedDepartmentDto.getIdRegion().getId());
        assertEquals(departmentDto.getIdRegion().getRegionName().toUpperCase(), savedDepartmentDto.getIdRegion().getRegionName());
    }

    /**
     * Vérifie que la récupération d'un département par ID, s'il existe, retourne le DTO du département.
     */
    @Test
    void testGetById_WhenDepartmentExists_ShouldReturnDepartmentDTO() {
        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));

        Optional<DepartmentDTO> departmentDTO = departmentService.getById(department.getId());

        assertTrue(departmentDTO.isPresent());
        DepartmentDTO expectedDepartmentDto = DepartmentMapper.mapToDepartmentDto(department);
        assertThat(departmentDTO.get()).usingRecursiveComparison().isEqualTo(expectedDepartmentDto);
    }

    /**
     * Vérifie que la récupération d'un département par ID, si il n'existe pas, déclenche une {@link ResourceNotFoundException}.
     */
    @Test
    void testGetById_WhenDepartmentDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(departmentRepository.findById(department.getId())).thenReturn(Optional.empty());

        Integer departmentId = department.getId();
        assertThrows(ResourceNotFoundException.class, () -> departmentService.getById(departmentId));
    }

    /**
     * Vérifie que la récupération d'un département par code de département, s'il existe, retourne le DTO du département.
     */
    @Test
    void testGetByDepartmentCode_WhenDepartmentExists_ShouldReturnDepartmentDTO() {
        when(departmentRepository.findByDepartmentCode(department.getDepartmentCode())).thenReturn(Optional.of(department));

        Optional<DepartmentDTO> departmentDTO = departmentService.getByDepartmentCode(department.getDepartmentCode());

        assertTrue(departmentDTO.isPresent());
        DepartmentDTO expectedDepartmentDto = DepartmentMapper.mapToDepartmentDto(department);
        assertThat(departmentDTO.get()).usingRecursiveComparison().isEqualTo(expectedDepartmentDto);
    }

    /**
     * Vérifie que la récupération d'un département par code de département, s'il n'existe pas, déclenche une {@link ResourceNotFoundException}.
     */
    @Test
    void testGetByDepartmentCode_WhenDepartmentDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(departmentRepository.findByDepartmentCode(department.getDepartmentCode())).thenReturn(Optional.empty());

        String departmentCode = department.getDepartmentCode();
        assertThrows(ResourceNotFoundException.class, () -> departmentService.getByDepartmentCode(departmentCode));
    }
}
