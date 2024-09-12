package com.afpa.hebergement;

import com.afpa.hebergement.controller.ScheduleController;
import com.afpa.hebergement.exception.*;
import com.afpa.hebergement.model.dto.ScheduleDTO;
import com.afpa.hebergement.model.dto.IntendantDTO;
import com.afpa.hebergement.service.entity_service.ScheduleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Time;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScheduleController.class) // Indique que c'est un test pour le contrôleur ScheduleController
@ExtendWith(MockitoExtension.class) // Utilisation de Mockito dans ce test
public class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc; // Injecte MockMvc pour simuler les requêtes HTTP

    @MockBean
    private ScheduleService scheduleService; // Création d'un mock du service ScheduleService

    // Test de création de schedule
    @Test
    @WithMockUser(username = "user", roles = {"USER"}) // Simule un utilisateur authentifié avec le rôle "USER"
    public void testCreateSchedule() throws Exception {

        // Création d'un DTO Schedule pour le test
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(1);
        scheduleDTO.setStartMorning(Time.valueOf("08:00:00"));
        scheduleDTO.setEndMorning(Time.valueOf("12:00:00"));
        scheduleDTO.setStartAfternoon(Time.valueOf("13:00:00"));
        scheduleDTO.setEndAfternoon(Time.valueOf("17:00:00"));

        // Ajout d'un DTO Intendant pour l'intendant
        IntendantDTO intendantDTO = new IntendantDTO();
        intendantDTO.setId(1);
        scheduleDTO.setIdIntendant(intendantDTO);

        // Configuration du mock pour retourner le DTO Schedule lors de l'appel à create
        when(scheduleService.create(any(ScheduleDTO.class))).thenReturn(scheduleDTO);

        // Simulation d'une requête POST et vérification de la réponse
        mockMvc.perform(post("/api/schedules/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"startMorning\":\"08:00:00\",\"endMorning\":\"12:00:00\",\"startAfternoon\":\"13:00:00\",\"endAfternoon\":\"17:00:00\",\"idIntendant\":{\"id\":1}}")
                        .with(csrf())) // Ajout d'un jeton CSRF valide à la requête
                .andExpect(status().isCreated()) // Vérification que le statut de la réponse est 201 (Created)
                .andExpect(jsonPath("$.id").value(1)) // Vérification que l'ID dans la réponse est 1
                .andExpect(jsonPath("$.startMorning").value("08:00:00")) // Vérification que l'heure de début matinée dans la réponse est correcte
                .andExpect(jsonPath("$.endMorning").value("12:00:00")) // Vérification que l'heure de fin matinée dans la réponse est correcte
                .andExpect(jsonPath("$.startAfternoon").value("13:00:00")) // Vérification que l'heure de début après-midi dans la réponse est correcte
                .andExpect(jsonPath("$.endAfternoon").value("17:00:00")) // Vérification que l'heure de fin après-midi dans la réponse est correcte
                .andExpect(jsonPath("$.idIntendant.id").value(1)); // Vérification que l'ID de l'intendant est 1
    }

    // Test de création de schedule avec DuplicateException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testCreateSchedule_DuplicateException() throws Exception {
        // Configuration du mock pour lancer une DuplicateException
        doThrow(new DuplicateException("Schedule already exists for the given times and intendant")).when(scheduleService).create(any(ScheduleDTO.class));

        // Simulation d'une requête POST et vérification de la réponse pour l'exception
        mockMvc.perform(post("/api/schedules/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"startMorning\":\"08:00:00\",\"endMorning\":\"12:00:00\",\"startAfternoon\":\"13:00:00\",\"endAfternoon\":\"17:00:00\",\"idIntendant\":{\"id\":1}}")
                        .with(csrf()))
                .andExpect(status().isConflict()); // Vérification que le statut de la réponse est 409 (Conflict)
    }

    // Test de création de schedule avec CreationException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testCreateSchedule_CreationException() throws Exception {
        // Configuration du mock pour lancer une CreationException
        doThrow(new CreationException("Error occurred while creating schedule")).when(scheduleService).create(any(ScheduleDTO.class));

        // Simulation d'une requête POST et vérification de la réponse pour l'exception
        mockMvc.perform(post("/api/schedules/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"startMorning\":\"08:00:00\",\"endMorning\":\"12:00:00\",\"startAfternoon\":\"13:00:00\",\"endAfternoon\":\"17:00:00\",\"idIntendant\":{\"id\":1}}")
                        .with(csrf()))
                .andExpect(status().isUnprocessableEntity()); // Vérification que le statut de la réponse est 422 (Unprocessable Entity)
    }

    // Test pour obtenir tous les schedules
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAllSchedules() throws Exception {
        // Création d'un DTO Schedule pour le test
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(1);
        scheduleDTO.setStartMorning(Time.valueOf("08:00:00"));
        scheduleDTO.setEndMorning(Time.valueOf("12:00:00"));
        scheduleDTO.setStartAfternoon(Time.valueOf("13:00:00"));
        scheduleDTO.setEndAfternoon(Time.valueOf("17:00:00"));

        // Ajout d'un DTO Intendant pour l'intendant
        IntendantDTO intendantDTO = new IntendantDTO();
        intendantDTO.setId(1);
        scheduleDTO.setIdIntendant(intendantDTO);

        // Configuration du mock pour retourner une liste de schedules
        when(scheduleService.getAll()).thenReturn(List.of(scheduleDTO));

        // Simulation d'une requête GET et vérification de la réponse
        mockMvc.perform(get("/api/schedules"))
                .andExpect(status().isOk()) // Vérification que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$[0].id").value(1)) // Vérification que l'ID du premier schedule est 1
                .andExpect(jsonPath("$[0].startMorning").value("08:00:00")) // Vérification que l'heure de début matinée du premier schedule est correcte
                .andExpect(jsonPath("$[0].endMorning").value("12:00:00")) // Vérification que l'heure de fin matinée du premier schedule est correcte
                .andExpect(jsonPath("$[0].startAfternoon").value("13:00:00")) // Vérification que l'heure de début après-midi du premier schedule est correcte
                .andExpect(jsonPath("$[0].endAfternoon").value("17:00:00")) // Vérification que l'heure de fin après-midi du premier schedule est correcte
                .andExpect(jsonPath("$[0].idIntendant.id").value(1)); // Vérification que l'ID de l'intendant est 1
    }

    // Test pour obtenir tous les schedules avec NoContentException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAllSchedules_NoContentException() throws Exception {
        // Configuration du mock pour lancer une NoContentException
        when(scheduleService.getAll()).thenThrow(new NoContentException("No schedules found"));

        // Simulation d'une requête GET et vérification de la réponse pour l'exception
        mockMvc.perform(get("/api/schedules"))
                .andExpect(status().isNoContent()); // Vérification que le statut de la réponse est 204 (No Content)
    }

    // Test pour obtenir un schedule par ID
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetScheduleById() throws Exception {
        // Création d'un DTO Schedule pour le test
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(1);
        scheduleDTO.setStartMorning(Time.valueOf("08:00:00"));
        scheduleDTO.setEndMorning(Time.valueOf("12:00:00"));
        scheduleDTO.setStartAfternoon(Time.valueOf("13:00:00"));
        scheduleDTO.setEndAfternoon(Time.valueOf("17:00:00"));

        // Ajout d'un DTO Intendant pour l'intendant
        IntendantDTO intendantDTO = new IntendantDTO();
        intendantDTO.setId(1);
        scheduleDTO.setIdIntendant(intendantDTO);

        // Configuration du mock pour retourner un schedule
        when(scheduleService.getById(1)).thenReturn(Optional.of(scheduleDTO));

        // Simulation d'une requête GET et vérification de la réponse
        mockMvc.perform(get("/api/schedules/1"))
                .andExpect(status().isOk()) // Vérification que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$.id").value(1)) // Vérification que l'ID du schedule est 1
                .andExpect(jsonPath("$.startMorning").value("08:00:00")) // Vérification que l'heure de début matinée du schedule est correcte
                .andExpect(jsonPath("$.endMorning").value("12:00:00")) // Vérification que l'heure de fin matinée du schedule est correcte
                .andExpect(jsonPath("$.startAfternoon").value("13:00:00")) // Vérification que l'heure de début après-midi du schedule est correcte
                .andExpect(jsonPath("$.endAfternoon").value("17:00:00")) // Vérification que l'heure de fin après-midi du schedule est correcte
                .andExpect(jsonPath("$.idIntendant.id").value(1)); // Vérification que l'ID de l'intendant est 1
    }

    // Test pour obtenir un schedule par ID avec ResourceNotFoundException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetScheduleById_ResourceNotFoundException() throws Exception {
        // Configuration du mock pour lancer une ResourceNotFoundException
        when(scheduleService.getById(1)).thenThrow(new ResourceNotFoundException("Schedule not found with id 1"));

        // Simulation d'une requête GET et vérification de la réponse pour l'exception
        mockMvc.perform(get("/api/schedules/1"))
                .andExpect(status().isNotFound()); // Vérification que le statut de la réponse est 404 (Not Found)
    }


    // Test pour récupérer les schedules par l'ID de l'intendant
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetScheduleByIdIntendant() throws Exception {
        // Crée le premier objet ScheduleDTO avec les valeurs de test
        ScheduleDTO scheduleDTO1 = new ScheduleDTO();
        scheduleDTO1.setId(1);
        scheduleDTO1.setStartMorning(Time.valueOf("08:00:00"));
        scheduleDTO1.setEndMorning(Time.valueOf("12:00:00"));
        scheduleDTO1.setStartAfternoon(Time.valueOf("13:00:00"));
        scheduleDTO1.setEndAfternoon(Time.valueOf("17:00:00"));

        // Crée le premier objet IntendantDTO et l'associe au premier ScheduleDTO
        IntendantDTO intendantDTO1 = new IntendantDTO();
        intendantDTO1.setId(1);
        scheduleDTO1.setIdIntendant(intendantDTO1);

        // Crée le deuxième objet ScheduleDTO avec les valeurs de test
        ScheduleDTO scheduleDTO2 = new ScheduleDTO();
        scheduleDTO2.setId(2);
        scheduleDTO2.setStartMorning(Time.valueOf("09:00:00"));
        scheduleDTO2.setEndMorning(Time.valueOf("13:00:00"));
        scheduleDTO2.setStartAfternoon(Time.valueOf("14:00:00"));
        scheduleDTO2.setEndAfternoon(Time.valueOf("18:00:00"));

        // Crée le deuxième objet IntendantDTO et l'associe au deuxième ScheduleDTO
        IntendantDTO intendantDTO2 = new IntendantDTO();
        intendantDTO2.setId(2);
        scheduleDTO2.setIdIntendant(intendantDTO2);

        List<ScheduleDTO> schedules = Arrays.asList(scheduleDTO1, scheduleDTO2);

        // Configuration du mock pour retourner les schedules
        when(scheduleService.getByIdIntendant(1)).thenReturn(schedules);

        mockMvc.perform(get("/api/schedules/intendant/{idIntendant}", 1)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].startMorning").value("08:00:00"))
                .andExpect(jsonPath("$[0].endMorning").value("12:00:00"))
                .andExpect(jsonPath("$[0].startAfternoon").value("13:00:00"))
                .andExpect(jsonPath("$[0].endAfternoon").value("17:00:00"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].startMorning").value("09:00:00"))
                .andExpect(jsonPath("$[1].endMorning").value("13:00:00"))
                .andExpect(jsonPath("$[1].startAfternoon").value("14:00:00"))
                .andExpect(jsonPath("$[1].endAfternoon").value("18:00:00"));
    }


    // Test pour gérer le cas où le schedule n'est pas trouvé par son ID et celui de l'intendant
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetScheduleByIdAndIntendantId_NotFound() throws Exception {
        when(scheduleService.getByIdScheduleAndIdIntendant(any(Integer.class), any(Integer.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/schedules/{idSchedule}/intendant/{idIntendant}", 1, 1)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }


    // Test pour le cas où aucun schedule n'est trouvé pour l'intendant
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetScheduleByIdIntendant_NoContent() throws Exception {
        when(scheduleService.getByIdIntendant(any(Integer.class)))
                .thenThrow(new NoContentException("No schedules found"));

        mockMvc.perform(get("/api/schedules/intendant/{idIntendant}", 1)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }


    // Test pour le cas où une erreur interne se produit lors de la récupération des schedules
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetScheduleByIdIntendant_InternalServerError() throws Exception {
        when(scheduleService.getByIdIntendant(any(Integer.class)))
                .thenThrow(new InternalServerException("Error occurred while getting schedule by ID"));

        mockMvc.perform(get("/api/schedules/intendant/{idIntendant}", 1)
                        .with(csrf()))
                .andExpect(status().isInternalServerError());
    }


    // Test pour obtenir un schedule par ID et ID intendant
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetScheduleByIdAndIntendantId() throws Exception {
        // Crée un objet ScheduleDTO avec les valeurs de test pour le premier schedule
        ScheduleDTO scheduleDTO1 = new ScheduleDTO();
        scheduleDTO1.setId(1);
        scheduleDTO1.setStartMorning(Time.valueOf("08:00:00"));
        scheduleDTO1.setEndMorning(Time.valueOf("12:00:00"));
        scheduleDTO1.setStartAfternoon(Time.valueOf("13:00:00"));
        scheduleDTO1.setEndAfternoon(Time.valueOf("17:00:00"));

        // Crée un objet IntendantDTO et l'associe au premier ScheduleDTO
        IntendantDTO intendantDTO1 = new IntendantDTO();
        intendantDTO1.setId(1);
        scheduleDTO1.setIdIntendant(intendantDTO1);

        // Crée un objet ScheduleDTO avec les valeurs de test pour le deuxième schedule
        ScheduleDTO scheduleDTO2 = new ScheduleDTO();
        scheduleDTO2.setId(2);
        scheduleDTO2.setStartMorning(Time.valueOf("09:00:00"));
        scheduleDTO2.setEndMorning(Time.valueOf("13:00:00"));
        scheduleDTO2.setStartAfternoon(Time.valueOf("14:00:00"));
        scheduleDTO2.setEndAfternoon(Time.valueOf("18:00:00"));

        // Crée un objet IntendantDTO et l'associe au deuxième ScheduleDTO
        IntendantDTO intendantDTO2 = new IntendantDTO();
        intendantDTO2.setId(2);
        scheduleDTO2.setIdIntendant(intendantDTO2);

        // Configuration du mock pour retourner un Optional de ScheduleDTO pour le premier schedule
        when(scheduleService.getByIdScheduleAndIdIntendant(1, 1)).thenReturn(Optional.of(scheduleDTO1));

        mockMvc.perform(get("/api/schedules/1/intendant/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.startMorning").value("08:00:00"))
                .andExpect(jsonPath("$.endMorning").value("12:00:00"))
                .andExpect(jsonPath("$.startAfternoon").value("13:00:00"))
                .andExpect(jsonPath("$.endAfternoon").value("17:00:00"))
                .andExpect(jsonPath("$.idIntendant.id").value(1));

        // Configuration du mock pour retourner un Optional de ScheduleDTO pour le deuxième schedule
        when(scheduleService.getByIdScheduleAndIdIntendant(2, 2)).thenReturn(Optional.of(scheduleDTO2));

        mockMvc.perform(get("/api/schedules/2/intendant/2")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.startMorning").value("09:00:00"))
                .andExpect(jsonPath("$.endMorning").value("13:00:00"))
                .andExpect(jsonPath("$.startAfternoon").value("14:00:00"))
                .andExpect(jsonPath("$.endAfternoon").value("18:00:00"))
                .andExpect(jsonPath("$.idIntendant.id").value(2));
    }


    // Test de mise à jour d'un schedule
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdateSchedule() throws Exception {
        // Crée un objet ScheduleDTO avec les valeurs de test
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(1);
        scheduleDTO.setStartMorning(Time.valueOf("08:00:00"));
        scheduleDTO.setEndMorning(Time.valueOf("12:00:00"));
        scheduleDTO.setStartAfternoon(Time.valueOf("13:00:00"));
        scheduleDTO.setEndAfternoon(Time.valueOf("17:00:00"));

        // Crée un objet IntendantDTO et l'associe au ScheduleDTO
        IntendantDTO intendantDTO = new IntendantDTO();
        intendantDTO.setId(1);
        scheduleDTO.setIdIntendant(intendantDTO);

        // Mock le comportement du service pour retourner le ScheduleDTO mis à jour
        when(scheduleService.update(any(Integer.class), any(ScheduleDTO.class))).thenReturn(Optional.of(scheduleDTO));

        // Effectue une requête PUT pour mettre à jour le Schedule
        mockMvc.perform(put("/api/schedules/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"startMorning\":\"08:00:00\",\"endMorning\":\"12:00:00\",\"startAfternoon\":\"13:00:00\",\"endAfternoon\":\"17:00:00\",\"idIntendant\":{\"id\":1}}")
                        .with(csrf()))
                .andExpect(status().isOk()) // Vérifie que le statut HTTP est 200 OK
                .andExpect(jsonPath("$.id").value(1)) // Vérifie que l'ID est 1
                .andExpect(jsonPath("$.startMorning").value("08:00:00")) // Vérifie l'heure de début du matin
                .andExpect(jsonPath("$.endMorning").value("12:00:00")) // Vérifie l'heure de fin du matin
                .andExpect(jsonPath("$.startAfternoon").value("13:00:00")) // Vérifie l'heure de début de l'après-midi
                .andExpect(jsonPath("$.endAfternoon").value("17:00:00")) // Vérifie l'heure de fin de l'après-midi
                .andExpect(jsonPath("$.idIntendant.id").value(1)); // Vérifie l'ID de l'intendant associé
    }

    // Test de mise à jour de schedule avec ResourceNotFoundException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdateSchedule_ResourceNotFoundException() throws Exception {
        // Mock le comportement du service pour lancer une ResourceNotFoundException
        doThrow(new ResourceNotFoundException("Schedule ID not found: 1")).when(scheduleService).update(any(Integer.class), any(ScheduleDTO.class));

        // Effectue une requête PUT pour mettre à jour le Schedule
        mockMvc.perform(put("/api/schedules/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"startMorning\":\"08:00:00\",\"endMorning\":\"12:00:00\",\"startAfternoon\":\"13:00:00\",\"endAfternoon\":\"17:00:00\",\"idIntendant\":{\"id\":1}}")
                        .with(csrf()))
                .andExpect(status().isNotFound()); // Vérifie que le statut HTTP est 404 Not Found
    }

    // Test de mise à jour de schedule avec DuplicateException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdateSchedule_DuplicateException() throws Exception {
        // Mock le comportement du service pour lancer une DuplicateException
        doThrow(new DuplicateException("Duplicate day ID found")).when(scheduleService).update(any(Integer.class), any(ScheduleDTO.class));

        // Effectue une requête PUT pour mettre à jour le Schedule
        mockMvc.perform(put("/api/schedules/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"startMorning\":\"08:00:00\",\"endMorning\":\"12:00:00\",\"startAfternoon\":\"13:00:00\",\"endAfternoon\":\"17:00:00\",\"idIntendant\":{\"id\":1}}")
                        .with(csrf()))
                .andExpect(status().isConflict()); // Vérifie que le statut HTTP est 409 Conflict
    }

    // Test de mise à jour de schedule avec InternalServerException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdateSchedule_InternalServerException() throws Exception {
        // Mock le comportement du service pour lancer une InternalServerException
        doThrow(new InternalServerException("Error occurred while updating schedule")).when(scheduleService).update(any(Integer.class), any(ScheduleDTO.class));

        // Effectue une requête PUT pour mettre à jour le Schedule
        mockMvc.perform(put("/api/schedules/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"startMorning\":\"08:00:00\",\"endMorning\":\"12:00:00\",\"startAfternoon\":\"13:00:00\",\"endAfternoon\":\"17:00:00\",\"idIntendant\":{\"id\":1}}")
                        .with(csrf()))
                .andExpect(status().isInternalServerError()); // Vérifie que le statut HTTP est 500 Internal Server Error
    }


//    // Test pour mettre à jour un schedule par ID et ID intendant
//    @Test
//    @WithMockUser(username = "user", roles = {"USER"})
//    public void testUpdateScheduleByIdAndIntendantId() throws Exception {
//        // Crée un objet ScheduleDTO avec les valeurs de test pour le schedule à mettre à jour
//        ScheduleDTO scheduleToUpdate = new ScheduleDTO();
//        scheduleToUpdate.setId(1);
//        scheduleToUpdate.setStartMorning(Time.valueOf("08:00:00"));
//        scheduleToUpdate.setEndMorning(Time.valueOf("12:00:00"));
//        scheduleToUpdate.setStartAfternoon(Time.valueOf("13:00:00"));
//        scheduleToUpdate.setEndAfternoon(Time.valueOf("17:00:00"));
//
//        // Crée un objet IntendantDTO et l'associe au ScheduleDTO à mettre à jour
//        IntendantDTO intendantDTO = new IntendantDTO();
//        intendantDTO.setId(1);
//        scheduleToUpdate.setIdIntendant(intendantDTO);
//
//        // Mock du service pour retourner le ScheduleDTO mis à jour
//        when(scheduleService.updateByIdScheduleAndIdIntendant(any(Integer.class), any(Integer.class), any(ScheduleDTO.class))).thenReturn(Optional.of(scheduleToUpdate));
//
//        mockMvc.perform(put("/api/schedules/update/{id}/intendant/{idIntendant}", 1, 1)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"id\":1,\"startMorning\":\"08:00:00\",\"endMorning\":\"12:00:00\",\"startAfternoon\":\"13:00:00\",\"endAfternoon\":\"17:00:00\",\"idIntendant\":{\"id\":1}}")
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.startMorning").value("08:00:00"))
//                .andExpect(jsonPath("$.endMorning").value("12:00:00"))
//                .andExpect(jsonPath("$.startAfternoon").value("13:00:00"))
//                .andExpect(jsonPath("$.endAfternoon").value("17:00:00"))
//                .andExpect(jsonPath("$.idIntendant.id").value(1));
//    }


    // Test de suppression d'un schedule par ID
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDeleteSchedule() throws Exception {

        doNothing().when(scheduleService).deleteById(1);

        mockMvc.perform(delete("/api/schedules/delete/1").with(csrf()))
                .andExpect(status().isNoContent());
    }


    // Test de suppression d'un schedule par ID avec ResourceNotFoundException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDeleteSchedule_ResourceNotFoundException() throws Exception {
        doThrow(new ResourceNotFoundException("Schedule not found with id 1")).when(scheduleService).deleteById(1);

        mockMvc.perform(delete("/api/schedules/delete/1").with(csrf()))
                .andExpect(status().isNotFound());
    }


    // Test pour supprimer un schedule par l'ID de l'intendant
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDeleteByIdIntendant() throws Exception {
        // Mock le comportement du service pour la suppression
        doNothing().when(scheduleService).deleteByIdIntendant(any(Integer.class));

        mockMvc.perform(delete("/api/schedules/delete/intendant/{idIntendant}", 1)
                        .with(csrf()))
                .andExpect(status().isNoContent()); // Vérifie que le statut HTTP est 204 No Content
    }


}
