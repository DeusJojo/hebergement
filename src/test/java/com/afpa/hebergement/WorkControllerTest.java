package com.afpa.hebergement;

import com.afpa.hebergement.controller.WorkController;
import com.afpa.hebergement.exception.CreationException;
import com.afpa.hebergement.exception.DuplicateException;
import com.afpa.hebergement.exception.NoContentException;
import com.afpa.hebergement.exception.ResourceNotFoundException;
import com.afpa.hebergement.model.dto.*;
import com.afpa.hebergement.service.entity_service.WorkService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WorkController.class) // Indique que c'est un test pour le contrôleur WorkController
@ExtendWith(MockitoExtension.class) // Utilisation de Mockito dans ce test
public class WorkControllerTest {

    @Autowired
    private MockMvc mockMvc; // Injecte MockMvc pour simuler les requêtes HTTP

    @MockBean
    private WorkService workService; // Création d'un mock du service WorkService


    // Test de création de travaux
    @Test
    @WithMockUser(username = "user", roles = {"USER"}) // Simule un utilisateur authentifié avec le rôle "USER"
    public void testCreateWork() throws Exception {
        // Création d'un DTO Work pour le test
        WorkDTO workDTO = new WorkDTO();
        workDTO.setId(1);
        workDTO.setStartWork(LocalDate.of(2024, 6, 1));
        workDTO.setEndWork(LocalDate.of(2024, 6, 10));
        workDTO.setWorkCommentary("Test commentary");

        // Création de DTOs pour les clés étrangères (RoomDTO et WorkTypeDTO)
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(1);
        workDTO.setIdRoom(roomDTO);

        WorkTypeDTO workTypeDTO = new WorkTypeDTO();
        workTypeDTO.setId(1);
        workDTO.setIdWorkType(workTypeDTO);

        // Configuration du mock pour retourner le DTO Work lors de l'appel à create
        when(workService.create(any(WorkDTO.class))).thenReturn(workDTO);

        // Simulation d'une requête POST et vérification de la réponse
        mockMvc.perform(post("/api/works/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"startWork\":\"2024-06-01\",\"endWork\":\"2024-06-10\",\"idRoom\":{\"id\":1},\"idWorkType\":{\"id\":1},\"workCommentary\":\"Test commentary\"}")
                        .with(csrf())) // Ajout d'un jeton CSRF valide à la requête
                .andExpect(status().isCreated()) // Vérification que le statut de la réponse est 201 (Created)
                .andExpect(jsonPath("$.id").value(1)) // Vérification que l'ID dans la réponse est 1
                .andExpect(jsonPath("$.startWork").value("2024-06-01")) // Vérification que la date de début dans la réponse est correcte
                .andExpect(jsonPath("$.endWork").value("2024-06-10")) // Vérification que la date de fin dans la réponse est correcte
                .andExpect(jsonPath("$.idRoom.id").value(1)) // Vérification que l'ID de la chambre est 1
                .andExpect(jsonPath("$.idWorkType.id").value(1)) // Vérification que l'ID du type de travail est 1
                .andExpect(jsonPath("$.workCommentary").value("Test commentary")); // Vérification que le commentaire est correct
    }


    // Test de création de travail avec DuplicateException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testCreateWork_DuplicateException() throws Exception {
        // Configuration du mock pour lancer une DuplicateException
        doThrow(new DuplicateException("Work already exists for the given dates, work type, and room")).when(workService).create(any(WorkDTO.class));

        // Simulation d'une requête POST et vérification de la réponse pour l'exception
        mockMvc.perform(post("/api/works/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"startWork\":\"2024-06-01\",\"endWork\":\"2024-06-10\",\"idRoom\":{\"id\":1},\"idWorkType\":{\"id\":1},\"workCommentary\":\"Test commentary\"}")
                        .with(csrf()))
                .andExpect(status().isConflict()); // Vérification que le statut de la réponse est 409 (Conflict)
    }


    // Test de création de travail avec CreationException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testCreateWork_CreationException() throws Exception {
        // Configuration du mock pour lancer une CreationException
        doThrow(new CreationException("Error occurred while creating work")).when(workService).create(any(WorkDTO.class));

        // Simulation d'une requête POST et vérification de la réponse pour l'exception
        mockMvc.perform(post("/api/works/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"startWork\":\"2024-06-01\",\"endWork\":\"2024-06-10\",\"idRoom\":{\"id\":1},\"idWorkType\":{\"id\":1},\"workCommentary\":\"Test commentary\"}")
                        .with(csrf()))
                .andExpect(status().isUnprocessableEntity()); // Vérification que le statut de la réponse est 422 (Unprocessable Entity)
    }


    // Test pour obtenir tous les travaux
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAllWorks() throws Exception {
        // Création d'un DTO Work pour le test
        WorkDTO workDTO = new WorkDTO();
        workDTO.setId(1);
        workDTO.setStartWork(LocalDate.of(2024, 6, 1));
        workDTO.setEndWork(LocalDate.of(2024, 6, 10));

        // Ajout d'un DTO Room pour la chambre
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(1);
        workDTO.setIdRoom(roomDTO);

        // Ajout d'un DTO WorkType pour le type de travail
        WorkTypeDTO workTypeDTO = new WorkTypeDTO();
        workTypeDTO.setId(1);
        workDTO.setIdWorkType(workTypeDTO);

        // Configuration du mock pour retourner une liste de travaux
        when(workService.getAll()).thenReturn(List.of(workDTO));

        // Simulation d'une requête GET et vérification de la réponse
        mockMvc.perform(get("/api/works"))
                .andExpect(status().isOk()) // Vérification que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$[0].id").value(1)) // Vérification que l'ID du premier travail est 1
                .andExpect(jsonPath("$[0].startWork").value("2024-06-01")) // Vérification que la date de début du premier travail est correcte
                .andExpect(jsonPath("$[0].endWork").value("2024-06-10")) // Vérification que la date de fin du premier travail est correcte
                .andExpect(jsonPath("$[0].idRoom.id").value(1)) // Vérification que l'ID de la chambre est 1
                .andExpect(jsonPath("$[0].idWorkType.id").value(1)); // Vérification que l'ID du type de travail est 1
    }


    // Test pour obtenir tous les travaux avec NoContentException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAllWorks_NoContentException() throws Exception {
        // Configuration du mock pour lancer une NoContentException
        when(workService.getAll()).thenThrow(new NoContentException("No works found"));

        // Simulation d'une requête GET et vérification de la réponse pour l'exception
        mockMvc.perform(get("/api/works"))
                .andExpect(status().isNoContent()); // Vérification que le statut de la réponse est 204 (No Content)
    }


    // Test pour obtenir tous les travaux avec pagination
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAllWorksWithPagination() throws Exception {
        // Création d'un DTO Work pour le test
        WorkDTO workDTO = new WorkDTO();
        workDTO.setId(1);
        workDTO.setStartWork(LocalDate.of(2024, 6, 1));
        workDTO.setEndWork(LocalDate.of(2024, 6, 10));

        // Ajout d'un DTO Room pour la chambre
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(1);
        workDTO.setIdRoom(roomDTO);

        // Ajout d'un DTO WorkType pour le type de travail
        WorkTypeDTO workTypeDTO = new WorkTypeDTO();
        workTypeDTO.setId(1);
        workDTO.setIdWorkType(workTypeDTO);

        // Configuration du mock pour retourner une page de travaux
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        when(workService.getAll(0, 10)).thenReturn(new PageImpl<>(Collections.singletonList(workDTO), pageable, 1));

        // Simulation d'une requête GET avec pagination et vérification de la réponse
        mockMvc.perform(get("/api/works/page?page=0&size=10"))
                .andExpect(status().isOk()) // Vérification que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$.content[0].id").value(1)) // Vérification que l'ID du premier travail est 1
                .andExpect(jsonPath("$.content[0].startWork").value("2024-06-01")) // Vérification que la date de début du premier travail est correcte
                .andExpect(jsonPath("$.content[0].endWork").value("2024-06-10")) // Vérification que la date de fin du premier travail est correcte
                .andExpect(jsonPath("$.content[0].idRoom.id").value(1)) // Vérification que l'ID de la chambre est 1
                .andExpect(jsonPath("$.content[0].idWorkType.id").value(1)); // Vérification que l'ID du type de travail est 1
    }


    // Test pour obtenir tous les travaux avec pagination et NoContentException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAllWorksWithPagination_NoContentException() throws Exception {
        // Configuration du mock pour lancer une NoContentException
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        when(workService.getAll(0, 10)).thenThrow(new NoContentException("No works found"));

        // Simulation d'une requête GET avec pagination et vérification de la réponse pour l'exception
        mockMvc.perform(get("/api/works/page?page=0&size=10"))
                .andExpect(status().isNoContent()); // Vérification que le statut de la réponse est 204 (No Content)
    }


    // Test de récupération des travaux par centre Afpa
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetWorksByAfpaCenter() throws Exception {
        // Préparation d'un WorkDTO pour le test
        WorkDTO workDTO = new WorkDTO();
        workDTO.setId(1);
        workDTO.setStartWork(LocalDate.of(2023, 7, 1));
        workDTO.setEndWork(LocalDate.of(2023, 7, 10));
        workDTO.setWorkCommentary("Test commentary");

        // Ajout d'un DTO WorkType pour le type de travail
        WorkTypeDTO workTypeDTO = new WorkTypeDTO();
        workTypeDTO.setId(1);
        workDTO.setIdWorkType(workTypeDTO);

        // Ajout d'un DTO Room pour la chambre
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(1);
        workDTO.setIdRoom(roomDTO);

        // Préparation d'un AfpaCenterDTO pour le test
        AfpaCenterDTO afpaCenter = new AfpaCenterDTO();
        afpaCenter.setId(3);
        afpaCenter.setCenterName("Center 3");

        // Préparation d'un floor pour le test
        FloorDTO floorDTO = new FloorDTO();
        floorDTO.setId(3);
        floorDTO.setIdAfpaCenter(afpaCenter);


        List<WorkDTO> works = Collections.singletonList(workDTO);

        // Configuration du mock pour retourner la liste de WorkDTO
        when(workService.getWorksByAfpaCenter(3)).thenReturn(works);

        // Effectuer une requête GET et vérifier la réponse
        mockMvc.perform(get("/api/works/afpa-center/{idAfpaCenter}", 3)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].startWork").value("2023-07-01"))
                .andExpect(jsonPath("$[0].endWork").value("2023-07-10"))
                .andExpect(jsonPath("$[0].workCommentary").value("Test commentary"))
                .andExpect(jsonPath("$[0].idRoom.id").value(1)) // Vérification que l'ID de la chambre est 1
                .andExpect(jsonPath("$[0].idWorkType.id").value(1)); // Vérification que l'ID du type de travail est 1
    }


    // Test de récupération des travaux par centre Afpa avec NoContentException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetWorksByAfpaCenter_NoContentException() throws Exception {
        // Configuration du mock pour lancer une NoContentException
        doThrow(new NoContentException("No works found")).when(workService).getWorksByAfpaCenter(1);

        // Effectuer une requête GET et vérifier le statut de la réponse
        mockMvc.perform(get("/api/works/afpa-center/{idAfpaCenter}", 1))
                .andExpect(status().isNoContent()); // 204 No Content
    }


    // Test de mise à jour de travail
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdateWork() throws Exception {
        // Création d'un DTO Work pour le test
        WorkDTO workDTO = new WorkDTO();
        workDTO.setId(1);
        workDTO.setStartWork(LocalDate.of(2024, 6, 1));
        workDTO.setEndWork(LocalDate.of(2024, 6, 10));

        // Ajout d'un DTO Room pour la chambre
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(1);
        workDTO.setIdRoom(roomDTO);

        // Ajout d'un DTO WorkType pour le type de travail
        WorkTypeDTO workTypeDTO = new WorkTypeDTO();
        workTypeDTO.setId(1);
        workDTO.setIdWorkType(workTypeDTO);

        // Configuration du mock pour retourner le DTO Work mis à jour
        when(workService.update(any(Integer.class), any(WorkDTO.class))).thenReturn(Optional.of(workDTO));

        // Simulation d'une requête PUT et vérification de la réponse
        mockMvc.perform(put("/api/works/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"startWork\":\"2024-06-01\",\"endWork\":\"2024-06-10\",\"idRoom\":{\"id\":1},\"idWorkType\":{\"id\":1}}")
                        .with(csrf()))
                .andExpect(status().isOk()) // Vérification que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$.id").value(1)) // Vérification que l'ID du travail mis à jour est 1
                .andExpect(jsonPath("$.startWork").value("2024-06-01")) // Vérification que la date de début du travail mis à jour est correcte
                .andExpect(jsonPath("$.endWork").value("2024-06-10")) // Vérification que la date de fin du travail mis à jour est correcte
                .andExpect(jsonPath("$.idRoom.id").value(1)) // Vérification que l'ID de la chambre est 1
                .andExpect(jsonPath("$.idWorkType.id").value(1)); // Vérification que l'ID du type de travail est 1
    }


    // Test de mise à jour de travail avec ResourceNotFoundException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdateWork_NotFoundException() throws Exception {
        // Configuration du mock pour lancer une ResourceNotFoundException
        when(workService.update(any(Integer.class), any(WorkDTO.class))).thenThrow(new ResourceNotFoundException("Work not found with ID: 1"));

        // Simulation d'une requête PUT et vérification de la réponse pour l'exception
        mockMvc.perform(put("/api/works/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"startWork\":\"2024-06-01\",\"endWork\":\"2024-06-10\",\"idRoom\":{\"id\":1},\"idWorkType\":{\"id\":1}}")
                        .with(csrf()))
                .andExpect(status().isNotFound()); // Vérification que le statut de la réponse est 404 (Not Found)
    }


    // Test de suppression de travail par ID
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDeleteWork() throws Exception {
        // Simulation d'une requête DELETE et vérification de la réponse
        mockMvc.perform(delete("/api/works/delete/1")
                        .with(csrf()))
                .andExpect(status().isNoContent()); // Vérification que le statut de la réponse est 204 (No Content)
    }


    // Test de suppression de travail avec ResourceNotFoundException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDeleteWork_NotFoundException() throws Exception {
        // Configuration du mock pour lancer une ResourceNotFoundException
        doThrow(new ResourceNotFoundException("Work not found with ID: 1")).when(workService).deleteById(any(Integer.class));

        // Simulation d'une requête DELETE et vérification de la réponse pour l'exception
        mockMvc.perform(delete("/api/works/delete/1")
                        .with(csrf()))
                .andExpect(status().isNotFound()); // Vérification que le statut de la réponse est 404 (Not Found)
    }
}
