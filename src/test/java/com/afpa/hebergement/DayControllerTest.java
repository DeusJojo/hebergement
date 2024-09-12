package com.afpa.hebergement;

import com.afpa.hebergement.controller.DayController;
import com.afpa.hebergement.exception.CreationException;
import com.afpa.hebergement.exception.DuplicateException;
import com.afpa.hebergement.exception.NoContentException;
import com.afpa.hebergement.exception.ResourceNotFoundException;
import com.afpa.hebergement.model.dto.DayDTO;
import com.afpa.hebergement.service.entity_service.DayService;
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

@WebMvcTest(DayController.class) // Indique que c'est un test pour le contrôleur DayController
@ExtendWith(MockitoExtension.class) // Utilisation de Mockito dans ce test
public class DayControllerTest {


    @Autowired
    private MockMvc mockMvc; // Injecte MockMvc pour simuler les requêtes HTTP

    @MockBean
    private DayService dayService; // Création d'un mock du service DayService

    // Test de création d'un jour
    @Test
    @WithMockUser(username = "user", roles = {"USER"}) // Simule un utilisateur authentifié avec le rôle "USER"
    public void testCreateDay() throws Exception {
        // Création d'un DTO Day pour le test
        DayDTO dayDTO = new DayDTO();
        dayDTO.setId(1);
        dayDTO.setWordingDay("Monday");

        // Configuration du mock pour retourner le DTO Day lors de l'appel à create
        when(dayService.create(any(DayDTO.class))).thenReturn(dayDTO);

        // Simulation d'une requête POST et vérification de la réponse
        mockMvc.perform(post("/api/days/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"wordingDay\":\"Monday\"}")
                        .with(csrf())) // Ajout d'un jeton CSRF valide à la requête
                .andExpect(status().isCreated()) // Vérification que le statut de la réponse est 201 (Created)
                .andExpect(jsonPath("$.id").value(1)) // Vérification que l'ID dans la réponse est 1
                .andExpect(jsonPath("$.wordingDay").value("Monday")); // Vérification que le libellé du jour dans la réponse est correct
    }

    // Test de création de jour avec DuplicateException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testCreateDay_DuplicateException() throws Exception {
        // Configuration du mock pour lancer une DuplicateException
        doThrow(new DuplicateException("Day already exists")).when(dayService).create(any(DayDTO.class));

        // Simulation d'une requête POST et vérification de la réponse pour l'exception
        mockMvc.perform(post("/api/days/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"wordingDay\":\"Monday\"}")
                        .with(csrf()))
                .andExpect(status().isConflict()); // Vérification que le statut de la réponse est 409 (Conflict)
    }

    // Test de création de jour avec CreationException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testCreateDay_CreationException() throws Exception {
        // Configuration du mock pour lancer une CreationException
        doThrow(new CreationException("Error occurred while creating day")).when(dayService).create(any(DayDTO.class));

        // Simulation d'une requête POST et vérification de la réponse pour l'exception
        mockMvc.perform(post("/api/days/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"wordingDay\":\"Monday\"}")
                        .with(csrf()))
                .andExpect(status().isUnprocessableEntity()); // Vérification que le statut de la réponse est 422 (Unprocessable Entity)
    }

    // Test pour obtenir tous les jours
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAllDays() throws Exception {
        // Création d'un DTO Day pour le test
        DayDTO dayDTO = new DayDTO();
        dayDTO.setId(1);
        dayDTO.setWordingDay("Monday");

        // Configuration du mock pour retourner une liste de jours
        when(dayService.getAll()).thenReturn(List.of(dayDTO));

        // Simulation d'une requête GET et vérification de la réponse
        mockMvc.perform(get("/api/days"))
                .andExpect(status().isOk()) // Vérification que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$[0].id").value(1)) // Vérification que l'ID du premier jour est 1
                .andExpect(jsonPath("$[0].wordingDay").value("Monday")); // Vérification que le libellé du premier jour est correct
    }

    // Test pour obtenir tous les jours avec NoContentException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAllDays_NoContentException() throws Exception {
        // Configuration du mock pour lancer une NoContentException
        when(dayService.getAll()).thenThrow(new NoContentException("No days found"));

        // Simulation d'une requête GET et vérification de la réponse pour l'exception
        mockMvc.perform(get("/api/days"))
                .andExpect(status().isNoContent()); // Vérification que le statut de la réponse est 204 (No Content)
    }


    // Test de mise à jour d'un jour
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdateDay() throws Exception {
        // Création d'un DTO Day pour le test
        DayDTO dayDTO = new DayDTO();
        dayDTO.setId(1);
        dayDTO.setWordingDay("Monday");

        // Configuration du mock pour retourner le DTO Day mis à jour
        when(dayService.update(any(Integer.class), any(DayDTO.class))).thenReturn(Optional.of(dayDTO));

        // Simulation d'une requête PUT et vérification de la réponse
        mockMvc.perform(put("/api/days/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"wordingDay\":\"Monday\"}")
                        .with(csrf()))
                .andExpect(status().isOk()) // Vérification que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$.id").value(1)) // Vérification que l'ID du jour mis à jour est 1
                .andExpect(jsonPath("$.wordingDay").value("Monday")); // Vérification que le libellé du jour mis à jour est correct
    }

    // Test de mise à jour d'un jour avec ResourceNotFoundException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdateDay_NotFoundException() throws Exception {
        // Configuration du mock pour lancer une ResourceNotFoundException
        when(dayService.update(any(Integer.class), any(DayDTO.class))).thenThrow(new ResourceNotFoundException("Day not found with ID: 1"));

        // Simulation d'une requête PUT et vérification de la réponse pour l'exception
        mockMvc.perform(put("/api/days/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"wordingDay\":\"Monday\"}")
                        .with(csrf()))
                .andExpect(status().isNotFound()); // Vérification que le statut de la réponse est 404 (Not Found)
    }

    // Test de suppression d'un jour par ID
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDeleteDay() throws Exception {
        // Simulation d'une requête DELETE et vérification de la réponse
        mockMvc.perform(delete("/api/days/delete/1")
                        .with(csrf()))
                .andExpect(status().isNoContent()); // Vérification que le statut de la réponse est 204 (No Content)
    }

    // Test de suppression d'un jour avec ResourceNotFoundException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDeleteDay_NotFoundException() throws Exception {
        // Configuration du mock pour lancer une ResourceNotFoundException
        doThrow(new ResourceNotFoundException("Day not found with ID: 1")).when(dayService).deleteById(any(Integer.class));

        // Simulation d'une requête DELETE et vérification de la réponse pour l'exception
        mockMvc.perform(delete("/api/days/delete/1")
                        .with(csrf()))
                .andExpect(status().isNotFound()); // Vérification que le statut de la réponse est 404 (Not Found)
    }

}
