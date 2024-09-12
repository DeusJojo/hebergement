package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.*;
import com.afpa.hebergement.model.dto.RegionDTO;
import com.afpa.hebergement.model.entity.Region;
import com.afpa.hebergement.model.mapper.RegionMapper;
import com.afpa.hebergement.model.repository.RegionRepository;
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
 * Classe de test pour {@link RegionServiceImpl}.
 * Elle vérifie les différents cas d'utilisation et comportements attendus du service de gestion des régions.
 */
@ExtendWith(SpringExtension.class)
class RegionAppServiceImplTest {

    @Mock
    private RegionRepository regionRepository;

    @InjectMocks
    private RegionServiceImpl regionService;

    private RegionDTO regionDto;
    private Region region;

    /**
     * Méthode de configuration exécutée avant chaque test.
     * Initialise les objets nécessaires pour les tests.
     */
    @BeforeEach
    void setUp() {
        regionDto = new RegionDTO(1, "hauts-de-france");
        region = new Region();
        region.setId(1);
        region.setRegionName("hauts-de-france");
    }

    /**
     * Vérifie que la création d'une région avec un DTO nul déclenche une {@link CreationException}.
     */
    @Test
    void testCreate_WhenRegionDtoIsNull_ShouldThrowCreationException() {
        assertThrows(CreationException.class, () -> regionService.create(null));
    }

    /**
     * Vérifie que la création d'une région existante déclenche une {@link DuplicateException}.
     */
    @Test
    void testCreate_WhenRegionExists_ShouldThrowDuplicateException() {
        when(regionRepository.findByRegionName(regionDto.getRegionName())).thenReturn(Optional.of(region));
        assertThrows(DuplicateException.class, () -> regionService.create(regionDto));
    }

    /**
     * Vérifie que la création d'une région en cas de violation d'intégrité des données déclenche une {@link CreationException}.
     */
    @Test
    void testCreate_WhenDataIntegrityViolationExceptionThrown_ShouldThrowCreationException() {
        when(regionRepository.findByRegionName(regionDto.getRegionName())).thenReturn(Optional.empty());
        when(regionRepository.save(any(Region.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(CreationException.class, () -> regionService.create(regionDto));
    }

    /**
     * Vérifie que la création d'une région en cas d'exception générique déclenche une {@link InternalServerException}.
     */
    @Test
    void testCreate_WhenExceptionThrown_ShouldThrowInternalServerException() {
        when(regionRepository.findByRegionName(regionDto.getRegionName())).thenReturn(Optional.empty());
        when(regionRepository.save(any(Region.class))).thenThrow(RuntimeException.class);

        assertThrows(InternalServerException.class, () -> regionService.create(regionDto));
    }

    /**
     * Vérifie que la création d'une région valide retourne le DTO de la région créée.
     */
    @Test
    void testCreate_WhenValid_ShouldReturnRegionDTO() {
        when(regionRepository.findByRegionName(regionDto.getRegionName())).thenReturn(Optional.empty());
        when(regionRepository.save(any(Region.class))).thenReturn(region);

        RegionDTO savedRegionDto = regionService.create(regionDto);

        assertNotNull(savedRegionDto);
        assertEquals(regionDto.getRegionName(), savedRegionDto.getRegionName().toLowerCase());
        assertEquals(regionDto.getRegionName().toUpperCase(), savedRegionDto.getRegionName());
    }

    /**
     * Vérifie que la récupération d'une région par ID, si elle existe, retourne le DTO de la région.
     */
    @Test
    void testGetById_WhenRegionExists_ShouldReturnRegionDTO() {
        when(regionRepository.findById(region.getId())).thenReturn(Optional.of(region));

        Optional<RegionDTO> regionDTO = regionService.getById(region.getId());

        assertTrue(regionDTO.isPresent());
        RegionDTO expectedRegionDto = RegionMapper.mapToRegionDto(region);
        assertThat(regionDTO.get()).usingRecursiveComparison().isEqualTo(expectedRegionDto);
    }

    /**
     * Vérifie que la récupération d'une région par ID, si elle n'existe pas, déclenche une {@link ResourceNotFoundException}.
     */
    @Test
    void testGetById_WhenRegionDoesNotExist_ShouldThrowResourceNotFoundException() {
        Integer regionId = region.getId();
        when(regionRepository.findById(regionId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> regionService.getById(regionId));
    }

    /**
     * Vérifie que la récupération d'une région par code de région, si elle existe, retourne le DTO de la région.
     */
    @Test
    void testGetByRegionCode_WhenRegionExists_ShouldReturnRegionDTO() {
        when(regionRepository.findByRegionName(region.getRegionName())).thenReturn(Optional.of(region));

        Optional<RegionDTO> regionDTO = regionService.getByRegionName(region.getRegionName());

        assertTrue(regionDTO.isPresent());
        RegionDTO expectedRegionDto = RegionMapper.mapToRegionDto(region);
        assertThat(regionDTO.get()).usingRecursiveComparison().isEqualTo(expectedRegionDto);
    }

    /**
     * Vérifie que la récupération d'une région par code de région, si elle n'existe pas, déclenche une {@link ResourceNotFoundException}.
     */
    @Test
    void testGetByRegionCode_WhenRegionDoesNotExist_ShouldThrowResourceNotFoundException() {
        String regionCode = region.getRegionName();
        when(regionRepository.findByRegionName(regionCode)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> regionService.getByRegionName(regionCode));
    }

    /**
     * Vérifie que la suppression d'une région par ID, si elle existe, ne déclenche aucune exception.
     */
    @Test
    void testDeleteById_WhenRegionExists_ShouldNotThrowException() {
        when(regionRepository.findById(region.getId())).thenReturn(Optional.of(region));

        assertDoesNotThrow(() -> regionService.deleteById(region.getId()));
        verify(regionRepository, times(1)).delete(region);
    }

    /**
     * Vérifie que la suppression d'une région par ID, si elle n'existe pas, déclenche une {@link ResourceNotFoundException}.
     */
    @Test
    void testDeleteById_WhenRegionDoesNotExist_ShouldThrowResourceNotFoundException() {
        Integer regionId = region.getId();
        when(regionRepository.findById(regionId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> regionService.deleteById(regionId));
    }
}
