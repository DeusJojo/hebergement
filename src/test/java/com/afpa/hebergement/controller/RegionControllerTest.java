package com.afpa.hebergement.controller;

import com.afpa.hebergement.exception.DuplicateException;
import com.afpa.hebergement.model.dto.RegionDTO;
import com.afpa.hebergement.security.WebSecurityConfig;
import com.afpa.hebergement.service.entity_service.RegionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegionController.class)
@Import(WebSecurityConfig.class) // Importer la configuration de sécurité dans le contexte de test
class RegionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegionService regionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateRegion() throws Exception {

        // Prépare un objet regionDto avec des données spécifiques pour le test
        RegionDTO regionDto = new RegionDTO();
        regionDto.setRegionName("Normandie");

        // Simuler la réponse de la méthode create du service
        RegionDTO savedRegionDTO = new RegionDTO();
        savedRegionDTO.setId(1);
        savedRegionDTO.setRegionName("Normandie");

        when(regionService.create(any(RegionDTO.class))).thenReturn(savedRegionDTO);

        // Effectue une requête POST vers api/region/create avec l'objet regionDto converti en JSON
        mockMvc.perform(post("/api/region/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regionDto)))

                // Vérifie que la réponse HTTP a un statut 201 (Created)
                .andExpect(status().isCreated())

                // Vérifie que la réponse JSON contient les attributs attendus
                .andExpect((jsonPath("$.id", is(1))))
                .andExpect((jsonPath("$.regionName", is("Normandie"))));
    }

    @Test
    void testCreateRegion_Conflict() throws Exception {

        RegionDTO regionDto = new RegionDTO();
        regionDto.setRegionName("Normandie");

        doThrow(new DuplicateException("Region already exists.")).when(regionService).create(any(RegionDTO.class));

        mockMvc.perform(post("/api/region/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regionDto)))

                .andExpect(status().isConflict());
    }

    @Test
    void testGetById() throws Exception {
        RegionDTO regionDto = new RegionDTO();
        regionDto.setId(1);
        regionDto.setRegionName("Normandie");

        when(regionService.getById(1)).thenReturn(Optional.of(regionDto));

        mockMvc.perform(get("/api/region/{id}", 1)
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.regionName", is("Normandie")));
    }

    @Test
    void testGetByCode() throws Exception {
        RegionDTO regionDto = new RegionDTO();
        regionDto.setId(1);
        regionDto.setRegionName("Normandie");

        when(regionService.getByRegionName("Normandie")).thenReturn(Optional.of(regionDto));

        mockMvc.perform(get("/api/region/getByRegionName/{name}", "Normandie")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.regionName", is("Normandie")));
    }
}