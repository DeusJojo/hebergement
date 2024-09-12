package com.afpa.hebergement.controller;

import com.afpa.hebergement.exception.DuplicateException;
import com.afpa.hebergement.model.dto.DepartmentDTO;
import com.afpa.hebergement.model.dto.RegionDTO;
import com.afpa.hebergement.security.WebSecurityConfig;
import com.afpa.hebergement.service.entity_service.DepartmentService;
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

@WebMvcTest(DepartmentController.class)
@Import(WebSecurityConfig.class)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentService departmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreate() throws Exception {

        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setId(1);
        regionDTO.setRegionName("grand est");

        DepartmentDTO departmentDto = new DepartmentDTO();
        departmentDto.setDepartmentName("Meuse");
        departmentDto.setDepartmentCode("055");
        departmentDto.setIdRegion(regionDTO);

        DepartmentDTO savedDepartmentDto = new DepartmentDTO();
        savedDepartmentDto.setId(1);
        savedDepartmentDto.setDepartmentName("Meuse");
        savedDepartmentDto.setDepartmentCode("055");
        savedDepartmentDto.setIdRegion(regionDTO);

        when(departmentService.create(any(DepartmentDTO.class))).thenReturn(savedDepartmentDto);

        mockMvc.perform(post("/api/department/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departmentDto)))
                .andExpect(status().isCreated())
                .andExpect((jsonPath("$.id", is(1))))
                .andExpect((jsonPath("$.departmentName", is("Meuse"))))
                .andExpect((jsonPath("$.departmentCode", is("055"))))
                .andExpect((jsonPath("$.idRegionDTO.id", is(1))))
                .andExpect((jsonPath("$.idRegionDTO.regionName", is("grand est"))));
    }

    @Test
    void testCreateRegion_Conflict() throws Exception {

        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setId(1);
        regionDTO.setRegionName("grand est");

        DepartmentDTO departmentDto = new DepartmentDTO();
        departmentDto.setDepartmentName("Meuse");
        departmentDto.setDepartmentCode("055");
        departmentDto.setIdRegion(regionDTO);

        DepartmentDTO savedDepartmentDto = new DepartmentDTO();
        savedDepartmentDto.setId(1);
        savedDepartmentDto.setDepartmentName("Meuse");
        savedDepartmentDto.setDepartmentCode("055");
        savedDepartmentDto.setIdRegion(regionDTO);

        doThrow(new DuplicateException("Department already exists.")).when(departmentService).create(any(DepartmentDTO.class));

        mockMvc.perform(post("/api/department/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departmentDto)))

                .andExpect(status().isConflict());
    }

    @Test
    void testGetById() throws Exception {
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setId(1);
        regionDTO.setRegionName("grand est");

        DepartmentDTO departmentDto = new DepartmentDTO();
        departmentDto.setId(1);
        departmentDto.setDepartmentName("Meuse");
        departmentDto.setDepartmentCode("055");
        departmentDto.setIdRegion(regionDTO);

        when(departmentService.getById(1)).thenReturn(Optional.of(departmentDto));

        mockMvc.perform(get("/api/department/{id}", 1)
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.id", is(1))))
                .andExpect((jsonPath("$.departmentName", is("Meuse"))))
                .andExpect((jsonPath("$.departmentCode", is("055"))))
                .andExpect((jsonPath("$.idRegionDTO.id", is(1))))
                .andExpect((jsonPath("$.idRegionDTO.regionName", is("grand est"))));
    }

    @Test
    void testGetDepartmentByCode() throws Exception {
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setId(1);
        regionDTO.setRegionName("grand est");

        DepartmentDTO departmentDto = new DepartmentDTO();
        departmentDto.setId(1);
        departmentDto.setDepartmentName("Meuse");
        departmentDto.setDepartmentCode("055");
        departmentDto.setIdRegion(regionDTO);

        when(departmentService.getByDepartmentCode("055")).thenReturn(Optional.of(departmentDto));

        mockMvc.perform(get("/api/department/getByDepartmentCode/{code}", "055")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.id", is(1))))
                .andExpect((jsonPath("$.departmentName", is("Meuse"))))
                .andExpect((jsonPath("$.departmentCode", is("055"))))
                .andExpect((jsonPath("$.idRegionDTO.id", is(1))))
                .andExpect((jsonPath("$.idRegionDTO.regionName", is("grand est"))));
    }
}