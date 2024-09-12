package com.afpa.hebergement;

import com.afpa.hebergement.controller.ReservationMotiveController;
import com.afpa.hebergement.exception.CreationException;
import com.afpa.hebergement.exception.DuplicateException;
import com.afpa.hebergement.exception.NoContentException;
import com.afpa.hebergement.exception.ResourceNotFoundException;
import com.afpa.hebergement.model.dto.ReservationMotiveDTO;
import com.afpa.hebergement.service.entity_service.ReservationMotiveService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationMotiveController.class) // Indique que c'est un test pour le contrôleur ReservationMotiveController
@ExtendWith(MockitoExtension.class) // Utilisation de Mockito dans ce test
public class ReservationMotiveControllerTest {

    @Autowired
    private MockMvc mockMvc; // Injecte MockMvc pour simuler les requêtes HTTP

    @MockBean
    private ReservationMotiveService reservationMotiveService; // Création d'un mock du service ReservationMotiveService


    // Test de création de motif de réservation
    @Test
    @WithMockUser(username = "user", roles = {"USER"}) // Simule un utilisateur authentifié avec le rôle "USER"
    public void testCreateReservationMotive() throws Exception {
        // Création d'un DTO ReservationMotive pour le test
        ReservationMotiveDTO motiveDTO = new ReservationMotiveDTO();
        motiveDTO.setId(1);
        motiveDTO.setMotive("Examen de certification");

        // Configuration du mock pour retourner le DTO ReservationMotive lors de l'appel à create
        when(reservationMotiveService.create(any(ReservationMotiveDTO.class))).thenReturn(motiveDTO);

        // Simulation d'une requête POST et vérification de la réponse
        mockMvc.perform(post("/api/reservationMotives/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"motive\":\"Examen de certification\"}")
                        .with(csrf())) // Ajout d'un jeton CSRF valide à la requête
                .andExpect(status().isCreated()) // Vérification que le statut de la réponse est 201 (Created)
                .andExpect(jsonPath("$.id").value(1)) // Vérification que l'ID dans la réponse est 1
                .andExpect(jsonPath("$.motive").value("Examen de certification")); // Vérification que le motif dans la réponse est "Examen de certification"
    }


    // Test de création de motif de réservation avec DuplicateException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testCreateReservationMotive_DuplicateException() throws Exception {
        // Configuration du mock pour lancer une DuplicateException
        doThrow(new DuplicateException("Motive already exists")).when(reservationMotiveService).create(any(ReservationMotiveDTO.class));

        // Simulation d'une requête POST et vérification de la réponse pour l'exception
        mockMvc.perform(post("/api/reservationMotives/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"motive\":\"Examen de certification\"}")
                        .with(csrf()))
                .andExpect(status().isConflict()); // Vérification que le statut de la réponse est 409 (Conflict)
    }


    // Test de création de motif de réservation avec CreationException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testCreateReservationMotive_CreationException() throws Exception {
        // Configuration du mock pour lancer une CreationException
        doThrow(new CreationException("Error occurred while creating motive")).when(reservationMotiveService).create(any(ReservationMotiveDTO.class));

        // Simulation d'une requête POST et vérification de la réponse pour l'exception
        mockMvc.perform(post("/api/reservationMotives/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"motive\":\"Examen de certification\"}")
                        .with(csrf()))
                .andExpect(status().isUnprocessableEntity()); // Vérification que le statut de la réponse est 422 (Unprocessable Entity)
    }


    // Test pour obtenir tous les motifs de réservation
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAllReservationMotives() throws Exception {
        // Création d'un DTO ReservationMotive pour le test
        ReservationMotiveDTO motiveDTO = new ReservationMotiveDTO();
        motiveDTO.setId(1);
        motiveDTO.setMotive("Examen de certification");

        // Configuration du mock pour retourner une liste de motifs
        when(reservationMotiveService.getAll()).thenReturn(Collections.singletonList(motiveDTO));

        // Simulation d'une requête GET et vérification de la réponse
        mockMvc.perform(get("/api/reservationMotives"))
                .andExpect(status().isOk()) // Vérification que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$[0].id").value(1)) // Vérification que l'ID du premier motif est 1
                .andExpect(jsonPath("$[0].motive").value("Examen de certification")); // Vérification que le motif du premier motif est "Examen de certification"
    }


    // Test pour obtenir tous les motifs de réservation avec NoContentException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAllReservationMotives_NoContentException() throws Exception {
        // Configuration du mock pour lancer une NoContentException
        doThrow(new NoContentException("No motives found")).when(reservationMotiveService).getAll();

        // Simulation d'une requête GET et vérification de la réponse pour l'exception
        mockMvc.perform(get("/api/reservationMotives"))
                .andExpect(status().isNoContent()); // Vérification que le statut de la réponse est 204 (No Content)
    }


    // Test pour obtenir un motif de réservation par ID
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetReservationMotiveById() throws Exception {
        // Création d'un DTO ReservationMotive pour le test
        ReservationMotiveDTO motiveDTO = new ReservationMotiveDTO();
        motiveDTO.setId(1);
        motiveDTO.setMotive("Examen de certification");

        // Configuration du mock pour retourner un motif par ID
        when(reservationMotiveService.getById(1)).thenReturn(Optional.of(motiveDTO));

        // Simulation d'une requête GET avec ID et vérification de la réponse
        mockMvc.perform(get("/api/reservationMotives/1"))
                .andExpect(status().isOk()) // Vérification que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$.id").value(1)) // Vérification que l'ID du motif est 1
                .andExpect(jsonPath("$.motive").value("Examen de certification")); // Vérification que le motif du motif est "Examen de certification"
    }


    // Test pour obtenir un motif de réservation par ID avec ResourceNotFoundException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetReservationMotiveById_NotFoundException() throws Exception {
        // Configuration du mock pour lancer une ResourceNotFoundException
        when(reservationMotiveService.getById(1)).thenThrow(new ResourceNotFoundException("Motive not found with id: 1"));

        // Simulation d'une requête GET avec ID et vérification de la réponse pour l'exception
        mockMvc.perform(get("/api/reservationMotives/1"))
                .andExpect(status().isNotFound()); // Vérification que le statut de la réponse est 404 (Not Found)
    }


    // Test de mise à jour de motif de réservation
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdateReservationMotive() throws Exception {
        // Création d'un DTO ReservationMotive pour le test
        ReservationMotiveDTO motiveDTO = new ReservationMotiveDTO();
        motiveDTO.setId(1);
        motiveDTO.setMotive("Examen de certification");

        // Configuration du mock pour retourner un motif mis à jour
        when(reservationMotiveService.update(any(Integer.class), any(ReservationMotiveDTO.class))).thenReturn(Optional.of(motiveDTO));

        // Simulation d'une requête PUT et vérification de la réponse
        mockMvc.perform(put("/api/reservationMotives/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"motive\":\"Business Trip\"}")
                        .with(csrf()))
                .andExpect(status().isOk()) // Vérification que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$.id").value(1)) // Vérification que l'ID de la réponse est 1
                .andExpect(jsonPath("$.motive").value("Examen de certification")); // Vérification que le motif de la réponse est "Examen de certification"
    }


    // Test de mise à jour de motif de réservation avec ResourceNotFoundException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdateReservationMotive_NotFoundException() throws Exception {
        // Configuration du mock pour lancer une ResourceNotFoundException
        when(reservationMotiveService.update(any(Integer.class), any(ReservationMotiveDTO.class))).thenThrow(new ResourceNotFoundException("Motive not found with id: 1"));

        // Simulation d'une requête PUT et vérification de la réponse pour l'exception
        mockMvc.perform(put("/api/reservationMotives/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"motive\":\"Examen de certification\"}")
                        .with(csrf()))
                .andExpect(status().isNotFound()); // Vérification que le statut de la réponse est 404 (Not Found)
    }


    // Test de suppression de motif de réservation par ID
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDeleteReservationMotive() throws Exception {
        // Simulation d'une requête DELETE et vérification de la réponse
        mockMvc.perform(delete("/api/reservationMotives/delete/1")
                        .with(csrf()))
                .andExpect(status().isNoContent()); // Vérification que le statut de la réponse est 204 (No Content)
    }


    // Test de suppression de motif de réservation par ID avec ResourceNotFoundException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDeleteReservationMotive_NotFoundException() throws Exception {

        // Configuration du mock pour lancer une ResourceNotFoundException
        doThrow(new ResourceNotFoundException("Motive not found with id: 1")).when(reservationMotiveService).deleteById(1);

        // Simulation d'une requête DELETE et vérification de la réponse pour l'exception
        mockMvc.perform(delete("/api/reservationMotives/delete/1")
                        .with(csrf()))
                .andExpect(status().isNotFound()); // Vérification que le statut de la réponse est 404 (Not Found)
    }

}
