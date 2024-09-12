package com.afpa.hebergement;


import com.afpa.hebergement.controller.WorkTypeController;
import com.afpa.hebergement.exception.CreationException;
import com.afpa.hebergement.exception.DuplicateException;
import com.afpa.hebergement.exception.NoContentException;
import com.afpa.hebergement.exception.ResourceNotFoundException;
import com.afpa.hebergement.model.dto.WorkTypeDTO;
import com.afpa.hebergement.service.entity_service.WorkTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WorkTypeController.class) // Indique que c'est un test pour le contrôleur WorkTypeController
@ExtendWith(MockitoExtension.class) // Utilisation de Mockito dans ce test
public class WorkTypeControllerTest {

    @Autowired
    private MockMvc mockMvc; // Injecte MockMvc pour simuler les requêtes HTTP

    @MockBean
    private WorkTypeService workTypeService; // Création d'un mock du service WorkTypeService

    // Test de création de type de travail
    @Test
    @WithMockUser(username = "user", roles = {"USER"}) // Simule un utilisateur authentifié avec le rôle "USER"
    public void testCreateWorkType() throws Exception {
        // Création d'un DTO WorkType pour le test
        WorkTypeDTO workTypeDTO = new WorkTypeDTO();
        workTypeDTO.setId(1);
        workTypeDTO.setWordingWorkType("Type A");

        // Configuration du mock pour retourner le DTO WorkType lors de l'appel à create
        when(workTypeService.create(any(WorkTypeDTO.class))).thenReturn(workTypeDTO);

        // Simulation d'une requête POST et vérification de la réponse
        mockMvc.perform(post("/api/workTypes/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"wordingWorkType\":\"Type A\"}")
                        .with(csrf())) // Ajout d'un jeton CSRF valide à la requête
                .andExpect(status().isCreated()) // Vérification que le statut de la réponse est 201 (Created)
                .andExpect(jsonPath("$.id").value(1)) // Vérification que l'ID dans la réponse est 1
                .andExpect(jsonPath("$.wordingWorkType").value("Type A")); // Vérification que le wordingWorkType dans la réponse est "Type A"
    }

    // Test de création de type de travail avec DuplicateException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testCreateWorkType_DuplicateException() throws Exception {
        // Configuration du mock pour lancer une DuplicateException
        doThrow(new DuplicateException("Work type already exists")).when(workTypeService).create(any(WorkTypeDTO.class));

        // Simulation d'une requête POST et vérification de la réponse pour l'exception
        mockMvc.perform(post("/api/workTypes/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"wordingWorkType\":\"Type A\"}")
                        .with(csrf()))
                .andExpect(status().isConflict()); // Vérification que le statut de la réponse est 409 (Conflict)
    }

    // Test de création de type de travail avec CreationException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testCreateWorkType_CreationException() throws Exception {
        // Configuration du mock pour lancer une CreationException
        doThrow(new CreationException("Error occurred while creating work type")).when(workTypeService).create(any(WorkTypeDTO.class));

        // Simulation d'une requête POST et vérification de la réponse pour l'exception
        mockMvc.perform(post("/api/workTypes/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"wordingWorkType\":\"Type A\"}")
                        .with(csrf()))
                .andExpect(status().isUnprocessableEntity()); // Vérification que le statut de la réponse est 422 (Unprocessable Entity)
    }

    // Test pour obtenir tous les types de travail
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAllWorkTypes() throws Exception {
        // Création de plusieurs DTO WorkType pour le test
        WorkTypeDTO workTypeDTO1 = new WorkTypeDTO();
        workTypeDTO1.setId(1);
        workTypeDTO1.setWordingWorkType("Type A");

        WorkTypeDTO workTypeDTO2 = new WorkTypeDTO();
        workTypeDTO2.setId(2);
        workTypeDTO2.setWordingWorkType("Type B");

        List<WorkTypeDTO> workTypeDTOList = List.of(workTypeDTO1, workTypeDTO2);

        // Configuration du mock pour retourner la liste de DTO WorkType
        when(workTypeService.getAll()).thenReturn(workTypeDTOList);

        // Simulation d'une requête GET et vérification de la réponse
        mockMvc.perform(get("/api/workTypes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk()) // Vérification que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$.length()").value(2)) // Vérification que la liste retournée contient 2 éléments
                .andExpect(jsonPath("$[0].id").value(1)) // Vérification que l'ID du premier type de travail est 1
                .andExpect(jsonPath("$[0].wordingWorkType").value("Type A")) // Vérification que le wordingWorkType du premier type de travail est "Type A"
                .andExpect(jsonPath("$[1].id").value(2)) // Vérification que l'ID du deuxième type de travail est 2
                .andExpect(jsonPath("$[1].wordingWorkType").value("Type B")); // Vérification que le wordingWorkType du deuxième type de travail est "Type B"
    }

    // Test pour obtenir tous les types de travail avec NoContentException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAllWorkTypes_NoContentException() throws Exception {
        // Configuration du mock pour lancer une NoContentException
        when(workTypeService.getAll()).thenThrow(new NoContentException("No work types found"));

        // Simulation d'une requête GET et vérification de la réponse pour l'exception
        mockMvc.perform(get("/api/workTypes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNoContent()); // Vérification que le statut de la réponse est 204 (No Content)
    }

    // Test pour obtenir un type de travail par ID
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetWorkTypeById() throws Exception {
        // Création d'un DTO WorkType pour le test
        WorkTypeDTO workTypeDTO = new WorkTypeDTO();
        workTypeDTO.setId(1);
        workTypeDTO.setWordingWorkType("Type A");

        // Configuration du mock pour retourner le DTO WorkType
        when(workTypeService.getById(1)).thenReturn(Optional.of(workTypeDTO));

        // Simulation d'une requête GET et vérification de la réponse
        mockMvc.perform(get("/api/workTypes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk()) // Vérification que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$.id").value(1)) // Vérification que l'ID du type de travail est 1
                .andExpect(jsonPath("$.wordingWorkType").value("Type A")); // Vérification que le wordingWorkType est "Type A"
    }

    // Test pour obtenir un type de travail par ID avec ResourceNotFoundException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetWorkTypeById_ResourceNotFoundException() throws Exception {
        // Configuration du mock pour lancer une ResourceNotFoundException
        when(workTypeService.getById(1)).thenThrow(new ResourceNotFoundException("Work type not found with ID: 1"));

        // Simulation d'une requête GET et vérification de la réponse pour l'exception
        mockMvc.perform(get("/api/workTypes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNotFound()); // Vérification que le statut de la réponse est 404 (Not Found)
    }

    // Test de mise à jour de type de travail
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdateWorkType() throws Exception {
        // Création d'un DTO WorkType pour le test
        WorkTypeDTO workTypeDTO = new WorkTypeDTO();
        workTypeDTO.setId(1);
        workTypeDTO.setWordingWorkType("Type A");

        // Configuration du mock pour retourner le DTO WorkType mis à jour
        when(workTypeService.update(any(Integer.class), any(WorkTypeDTO.class))).thenReturn(Optional.of(workTypeDTO));

        // Simulation d'une requête PUT et vérification de la réponse
        mockMvc.perform(put("/api/workTypes/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"wordingWorkType\":\"Updated Type\"}")
                        .with(csrf()))
                .andExpect(status().isOk()) // Vérification que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$.id").value(1)) // Vérification que l'ID du type de travail est 1
                .andExpect(jsonPath("$.wordingWorkType").value("Type A")); // Vérification que le wordingWorkType est "Type A"
    }

    // Test de mise à jour de type de travail avec ResourceNotFoundException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdateWorkType_ResourceNotFoundException() throws Exception {
        // Configuration du mock pour lancer une ResourceNotFoundException
        when(workTypeService.update(any(Integer.class), any(WorkTypeDTO.class))).thenThrow(new ResourceNotFoundException("Work type not found with ID: 1"));

        // Simulation d'une requête PUT et vérification de la réponse pour l'exception
        mockMvc.perform(put("/api/workTypes/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"wordingWorkType\":\"Updated Type\"}")
                        .with(csrf()))
                .andExpect(status().isNotFound()); // Vérification que le statut de la réponse est 404 (Not Found)
    }

    // Test de suppression de type de travail par ID
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDeleteWorkType() throws Exception {
        // Simulation d'une requête DELETE et vérification de la réponse
        mockMvc.perform(delete("/api/workTypes/delete/1")
                        .with(csrf()))
                .andExpect(status().isNoContent()); // Vérification que le statut de la réponse est 204 (No Content)
    }

    // Test de suppression de type de travail par ID avec ResourceNotFoundException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDeleteWorkType_ResourceNotFoundException() throws Exception {
        // Configuration du mock pour lancer une ResourceNotFoundException
        doThrow(new ResourceNotFoundException("Work type not found with ID: 1")).when(workTypeService).deleteById(1);

        // Simulation d'une requête DELETE et vérification de la réponse pour l'exception
        mockMvc.perform(delete("/api/workTypes/delete/1")
                        .with(csrf()))
                .andExpect(status().isNotFound()); // Vérification que le statut de la réponse est 404 (Not Found)
    }
}

