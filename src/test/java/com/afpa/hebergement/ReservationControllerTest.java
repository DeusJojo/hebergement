package com.afpa.hebergement;

import com.afpa.hebergement.controller.ReservationController;
import com.afpa.hebergement.exception.CreationException;
import com.afpa.hebergement.exception.DuplicateException;
import com.afpa.hebergement.exception.NoContentException;
import com.afpa.hebergement.exception.ResourceNotFoundException;
import com.afpa.hebergement.model.dto.AfpaCenterDTO;
import com.afpa.hebergement.model.dto.ReservationDTO;
import com.afpa.hebergement.model.dto.RoomDTO;
import com.afpa.hebergement.model.dto.ReservationMotiveDTO;
import com.afpa.hebergement.service.entity_service.ReservationService;
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


@WebMvcTest(ReservationController.class) // Indique que c'est un test pour le contrôleur ReservationController
@ExtendWith(MockitoExtension.class) // Utilisation de Mockito dans ce test
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc; // Injecte MockMvc pour simuler les requêtes HTTP

    @MockBean
    private ReservationService reservationService; // Création d'un mock du service ReservationService

    // Test de création de réservation
    @Test
    @WithMockUser(username = "user", roles = {"USER"}) // Simule un utilisateur authentifié avec le rôle "USER"
    public void testCreateReservation() throws Exception {

        // Création d'un DTO Reservation pour le test
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setId(1);
        reservationDTO.setStartDate(LocalDate.of(2023, 6, 26));
        reservationDTO.setEndDate(LocalDate.of(2023, 6, 30));

        // Ajout d'un DTO Room pour la chambre
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(1);
        reservationDTO.setIdRoom(roomDTO);

        // Ajout d'un DTO ReservationMotive pour le motif de réservation
        ReservationMotiveDTO reservationMotiveDTO = new ReservationMotiveDTO();
        reservationMotiveDTO.setId(1);
        reservationDTO.setIdReservationMotive(reservationMotiveDTO);

        // Configuration du mock pour retourner le DTO Reservation lors de l'appel à create
        when(reservationService.create(any(ReservationDTO.class))).thenReturn(reservationDTO);

        // Simulation d'une requête POST et vérification de la réponse
        mockMvc.perform(post("/api/reservations/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"startDate\":\"2023-06-26\",\"endDate\":\"2023-06-30\",\"idRoom\":{\"id\":1},\"idReservationMotive\":{\"id\":1}}")
                        .with(csrf())) // Ajout d'un jeton CSRF valide à la requête
                .andExpect(status().isCreated()) // Vérification que le statut de la réponse est 201 (Created)
                .andExpect(jsonPath("$.id").value(1)) // Vérification que l'ID dans la réponse est 1
                .andExpect(jsonPath("$.startDate").value("2023-06-26")) // Vérification que la date de début dans la réponse est correcte
                .andExpect(jsonPath("$.endDate").value("2023-06-30")) // Vérification que la date de fin dans la réponse est correcte
                .andExpect(jsonPath("$.idRoom.id").value(1)) // Vérification que l'ID de la chambre est 1
                .andExpect(jsonPath("$.idReservationMotive.id").value(1)); // Vérification que l'ID du motif de réservation est 1
    }


    // Test de création de réservation avec DuplicateException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testCreateReservation_DuplicateException() throws Exception {
        // Configuration du mock pour lancer une DuplicateException
        doThrow(new DuplicateException("Reservation already exists for the given dates and room")).when(reservationService).create(any(ReservationDTO.class));

        // Simulation d'une requête POST et vérification de la réponse pour l'exception
        mockMvc.perform(post("/api/reservations/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"startDate\":\"2023-06-26\",\"endDate\":\"2023-06-30\",\"idRoom\":{\"id\":1},\"idReservationMotive\":{\"id\":1}}")
                        .with(csrf()))
                .andExpect(status().isConflict()); // Vérification que le statut de la réponse est 409 (Conflict)
    }


    // Test de création de réservation avec CreationException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testCreateReservation_CreationException() throws Exception {
        // Configuration du mock pour lancer une CreationException
        doThrow(new CreationException("Error occurred while creating reservation")).when(reservationService).create(any(ReservationDTO.class));

        // Simulation d'une requête POST et vérification de la réponse pour l'exception
        mockMvc.perform(post("/api/reservations/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"startDate\":\"2023-06-26\",\"endDate\":\"2023-06-30\",\"idRoom\":{\"id\":1},\"idReservationMotive\":{\"id\":1}}")
                        .with(csrf()))
                .andExpect(status().isUnprocessableEntity()); // Vérification que le statut de la réponse est 422 (Unprocessable Entity)
    }


    // Test pour obtenir toutes les réservations
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAllReservations() throws Exception {
        // Création d'un DTO Reservation pour le test
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setId(1);
        reservationDTO.setStartDate(LocalDate.of(2023, 6, 26));
        reservationDTO.setEndDate(LocalDate.of(2023, 6, 30));

        // Ajout d'un DTO Room pour la chambre
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(1);
        reservationDTO.setIdRoom(roomDTO);

        // Ajout d'un DTO ReservationMotive pour le motif de réservation
        ReservationMotiveDTO reservationMotiveDTO = new ReservationMotiveDTO();
        reservationMotiveDTO.setId(1);
        reservationDTO.setIdReservationMotive(reservationMotiveDTO);

        // Configuration du mock pour retourner une liste de réservations
        when(reservationService.getAll()).thenReturn(List.of(reservationDTO));

        // Simulation d'une requête GET et vérification de la réponse
        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk()) // Vérification que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$[0].id").value(1)) // Vérification que l'ID de la première réservation est 1
                .andExpect(jsonPath("$[0].startDate").value("2023-06-26")) // Vérification que la date de début de la première réservation est correcte
                .andExpect(jsonPath("$[0].endDate").value("2023-06-30")) // Vérification que la date de fin de la première réservation est correcte
                .andExpect(jsonPath("$[0].idRoom.id").value(1)) // Vérification que l'ID de la chambre est 1
                .andExpect(jsonPath("$[0].idReservationMotive.id").value(1)); // Vérification que l'ID du motif de réservation est 1
    }


    // Test pour obtenir toutes les réservations avec NoContentException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAllReservations_NoContentException() throws Exception {
        // Configuration du mock pour lancer une NoContentException
        when(reservationService.getAll()).thenThrow(new NoContentException("No reservations found"));

        // Simulation d'une requête GET et vérification de la réponse pour l'exception
        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isNoContent()); // Vérification que le statut de la réponse est 204 (No Content)
    }


    // Test de récupération d'une réservation par ID
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetReservationById() throws Exception {
        // Préparation d'un ReservationDTO pour le test
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setId(1);
        reservationDTO.setStartDate(LocalDate.of(2023, 7, 1));
        reservationDTO.setEndDate(LocalDate.of(2023, 7, 10));

        // Ajout d'un DTO Room pour la chambre
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(1);
        reservationDTO.setIdRoom(roomDTO);

        // Ajout d'un DTO ReservationMotive pour le motif de réservation
        ReservationMotiveDTO reservationMotiveDTO = new ReservationMotiveDTO();
        reservationMotiveDTO.setId(1);
        reservationDTO.setIdReservationMotive(reservationMotiveDTO);

        // Configuration du mock pour retourner le ReservationDTO lors de l'appel de la méthode getById avec l'ID 1
        when(reservationService.getById(1)).thenReturn(Optional.of(reservationDTO));

        // Effectuer une requête GET avec l'ID 1 et vérifier la réponse
        mockMvc.perform(get("/api/reservations/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.startDate").value("2023-07-01"))
                .andExpect(jsonPath("$.endDate").value("2023-07-10"))
                .andExpect(jsonPath("$.idRoom.id").value(1)) // Vérification que l'ID de la chambre est 1
                .andExpect(jsonPath("$.idReservationMotive.id").value(1)); // Vérification que l'ID du motif de réservation est 1
    }

    // Test de récupération d'une réservation par ID avec ResourceNotFoundException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetReservationById_NotFoundException() throws Exception {
        // Configuration du mock pour lancer une ResourceNotFoundException
        when(reservationService.getById(1)).thenThrow(new ResourceNotFoundException("Reservation not found with id 1"));

        // Effectuer une requête GET avec l'ID 1 et vérifier que le statut de la réponse est Not Found
        mockMvc.perform(get("/api/reservations/{id}", 1))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetReservationsByAfpaCenter() throws Exception {

        // Préparation d'une liste de ReservationDTO pour le test
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setId(1);
        reservationDTO.setStartDate(LocalDate.of(2023, 7, 1));
        reservationDTO.setEndDate(LocalDate.of(2023, 7, 10));

        // Ajout d'un DTO Room pour la chambre
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(1);
        reservationDTO.setIdRoom(roomDTO);

        // Préparation d'un AfpaCenterDTO pour le test
        AfpaCenterDTO afpaCenter = new AfpaCenterDTO();
        afpaCenter.setId(2);
        afpaCenter.setCenterName("Center 2");


        // Ajout d'un DTO ReservationMotive pour le motif de réservation
        ReservationMotiveDTO reservationMotiveDTO = new ReservationMotiveDTO();
        reservationMotiveDTO.setId(1);
        reservationDTO.setIdReservationMotive(reservationMotiveDTO);

        List<ReservationDTO> reservations = Collections.singletonList(reservationDTO);

        // Configuration du mock pour retourner la liste de ReservationDTO
        when(reservationService.getReservationsByAfpaCenter(2)).thenReturn(reservations);

        // Effectuer une requête GET et vérifier la réponse
        mockMvc.perform(get("/api/reservations/afpa-center/{idAfpaCenter}", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].startDate").value("2023-07-01"))
                .andExpect(jsonPath("$[0].endDate").value("2023-07-10"))
                .andExpect(jsonPath("$[0].idRoom.id").value(1)) // Vérification que l'ID de la chambre est 1
                .andExpect(jsonPath("$[0].idReservationMotive.id").value(1)); // Vérification que l'ID du motif de réservation est 1
    }


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetReservationsByAfpaCenter_ResourceNotFoundException() throws Exception {
        // Configuration du mock pour lancer une ResourceNotFoundException
        when(reservationService.getReservationsByAfpaCenter(1)).thenThrow(new ResourceNotFoundException("No reservations found"));

        // Effectuer une requête GET et vérifier que le statut de la réponse est Not Found
        mockMvc.perform(get("/api/reservations/afpa-center/{idAfpaCenter}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    // Test de récupération des réservations par centre Afpa avec NoContentException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetReservationsByAfpaCenter_NoContentException() throws Exception {
        // Configurer le mock pour lancer une NoContentException
        doThrow(new NoContentException("No reservations found")).when(reservationService).getReservationsByAfpaCenter(2);

        // Effectuer une requête GET et vérifier le statut de la réponse
        mockMvc.perform(get("/api/reservations/afpa-center/{idAfpaCenter}", 2))
                .andExpect(status().isNoContent()); // 204 No Content
    }


    // Test pour obtenir toutes les réservations avec pagination
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAllReservationsWithPagination() throws Exception {

        // Création d'un DTO Reservation pour le test
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setId(1);
        reservationDTO.setStartDate(LocalDate.of(2023, 6, 26));
        reservationDTO.setEndDate(LocalDate.of(2023, 6, 30));

        // Ajout d'un DTO Room pour la chambre
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(1);
        reservationDTO.setIdRoom(roomDTO);

        // Ajout d'un DTO ReservationMotive pour le motif de réservation
        ReservationMotiveDTO reservationMotiveDTO = new ReservationMotiveDTO();
        reservationMotiveDTO.setId(1);
        reservationDTO.setIdReservationMotive(reservationMotiveDTO);

        // Configuration du mock pour retourner une page de réservations
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        when(reservationService.getAll(0, 10)).thenReturn(new PageImpl<>(Collections.singletonList(reservationDTO), pageable, 1));

        // Simulation d'une requête GET avec pagination et vérification de la réponse
        mockMvc.perform(get("/api/reservations/page?page=0&size=10"))
                .andExpect(status().isOk()) // Vérification que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$.content[0].id").value(1)) // Vérification que l'ID de la première réservation est 1
                .andExpect(jsonPath("$.content[0].startDate").value("2023-06-26")) // Vérification que la date de début de la première réservation est correcte
                .andExpect(jsonPath("$.content[0].endDate").value("2023-06-30")) // Vérification que la date de fin de la première réservation est correcte
                .andExpect(jsonPath("$.content[0].idRoom.id").value(1)) // Vérification que l'ID de la chambre est 1
                .andExpect(jsonPath("$.content[0].idReservationMotive.id").value(1)); // Vérification que l'ID du motif de réservation est 1
    }


    // Test pour obtenir toutes les réservations avec pagination et NoContentException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAllReservationsWithPagination_NoContentException() throws Exception {
        // Configuration du mock pour lancer une NoContentException
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        when(reservationService.getAll(0, 10)).thenThrow(new NoContentException("No reservations found"));

        // Simulation d'une requête GET avec pagination et vérification de la réponse pour l'exception
        mockMvc.perform(get("/api/reservations/page?page=0&size=10"))
                .andExpect(status().isNoContent()); // Vérification que le statut de la réponse est 204 (No Content)
    }


    // Test de mise à jour de réservation
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdateReservation() throws Exception {
        // Création d'un DTO Reservation pour le test
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setId(1);
        reservationDTO.setStartDate(LocalDate.of(2023, 6, 26));
        reservationDTO.setEndDate(LocalDate.of(2023, 6, 30));

        // Ajout d'un DTO Room pour la chambre
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(1);
        reservationDTO.setIdRoom(roomDTO);

        // Ajout d'un DTO ReservationMotive pour le motif de réservation
        ReservationMotiveDTO reservationMotiveDTO = new ReservationMotiveDTO();
        reservationMotiveDTO.setId(1);
        reservationDTO.setIdReservationMotive(reservationMotiveDTO);

        // Configuration du mock pour retourner le DTO Reservation mis à jour
        when(reservationService.update(any(Integer.class), any(ReservationDTO.class))).thenReturn(Optional.of(reservationDTO));

        // Simulation d'une requête PUT et vérification de la réponse
        mockMvc.perform(put("/api/reservations/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"startDate\":\"2023-06-26\",\"endDate\":\"2023-06-30\",\"idRoom\":{\"id\":1},\"idReservationMotive\":{\"id\":1}}")
                        .with(csrf()))
                .andExpect(status().isOk()) // Vérification que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$.id").value(1)) // Vérification que l'ID de la réservation mise à jour est 1
                .andExpect(jsonPath("$.startDate").value("2023-06-26")) // Vérification que la date de début de la réservation mise à jour est correcte
                .andExpect(jsonPath("$.endDate").value("2023-06-30")) // Vérification que la date de fin de la réservation mise à jour est correcte
                .andExpect(jsonPath("$.idRoom.id").value(1)) // Vérification que l'ID de la chambre est 1
                .andExpect(jsonPath("$.idReservationMotive.id").value(1)); // Vérification que l'ID du motif de réservation est 1
    }


    // Test de mise à jour de réservation avec ResourceNotFoundException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdateReservation_NotFoundException() throws Exception {
        // Configuration du mock pour lancer une ResourceNotFoundException
        when(reservationService.update(any(Integer.class), any(ReservationDTO.class))).thenThrow(new ResourceNotFoundException("Reservation not found with ID: 1"));

        // Simulation d'une requête PUT et vérification de la réponse pour l'exception
        mockMvc.perform(put("/api/reservations/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"startDate\":\"2023-06-26\",\"endDate\":\"2023-06-30\",\"idRoom\":{\"id\":1},\"idReservationMotive\":{\"id\":1}}")
                        .with(csrf()))
                .andExpect(status().isNotFound()); // Vérification que le statut de la réponse est 404 (Not Found)
    }


    // Test de suppression de réservation par ID
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDeleteReservation() throws Exception {
        // Simulation d'une requête DELETE et vérification de la réponse
        mockMvc.perform(delete("/api/reservations/delete/1")
                        .with(csrf()))
                .andExpect(status().isNoContent()); // Vérification que le statut de la réponse est 204 (No Content)
    }


    // Test de suppression de réservation avec ResourceNotFoundException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDeleteReservation_NotFoundException() throws Exception {
        // Configuration du mock pour lancer une ResourceNotFoundException
        doThrow(new ResourceNotFoundException("Reservation not found with ID: 1")).when(reservationService).deleteById(any(Integer.class));

        // Simulation d'une requête DELETE et vérification de la réponse pour l'exception
        mockMvc.perform(delete("/api/reservations/delete/1")
                        .with(csrf()))
                .andExpect(status().isNotFound()); // Vérification que le statut de la réponse est 404 (Not Found)
    }
}

