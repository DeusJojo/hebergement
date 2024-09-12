package com.afpa.hebergement;

import com.afpa.hebergement.controller.FloorController;
import com.afpa.hebergement.exception.CreationException;
import com.afpa.hebergement.model.dto.FloorDTO;
import com.afpa.hebergement.model.dto.AfpaCenterDTO;
import com.afpa.hebergement.service.entity_service.FloorService;
import com.afpa.hebergement.exception.DuplicateException;
import com.afpa.hebergement.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Annotation pour configurer le test
@WebMvcTest(FloorController.class) // Indique que nous testons le FloorController
public class FloorControllerTest {

    @Autowired
    private MockMvc mockMvc; // Injection de MockMvc pour simuler les requêtes HTTP
    @MockBean
    private FloorService floorService; // Mock du FloorService pour simuler les interactions avec la base de données

    // Test pour la création d'un étage
    @Test
    @WithMockUser(username = "user", roles = {"USER"}) // Simule un utilisateur authentifié avec le rôle "USER"
    public void testCreateFloor() throws Exception {
        // Création d'un AfpaCenterDTO pour le test
        AfpaCenterDTO afpaCenterDTO = new AfpaCenterDTO();
        afpaCenterDTO.setId(1);

        // Création d'un FloorDTO pour le test
        FloorDTO floorDTO = new FloorDTO();
        floorDTO.setId(1);
        floorDTO.setNumberFloor(1);
        floorDTO.setIsForWomen(true);
        floorDTO.setIdAfpaCenter(afpaCenterDTO);

        // Configuration du mock pour retourner le FloorDTO lors de l'appel de create
        when(floorService.create(any(FloorDTO.class))).thenReturn(floorDTO);

        // Simulation d'une requête POST et vérification de la réponse
        mockMvc.perform(post("/api/floors/create") // Simulation d'une requête POST vers "/api/floors/create"
                        .contentType(MediaType.APPLICATION_JSON) // Indique que le contenu de la requête est en JSON
                        .content("{\"id\":1,\"numberFloor\":1,\"isForWomen\":true,\"afpaCenter\":{\"id\":1}}") // Fournit le corps de la requête en JSON
                        .with(csrf())) // Ajoute un jeton CSRF valide à la requête
                .andExpect(status().isCreated()) // Vérifie que le statut de la réponse est 201 (Created)
                .andExpect(jsonPath("$.id").value(1)) // Vérifie que l'id dans la réponse est 1
                .andExpect(jsonPath("$.numberFloor").value(1)) // Vérifie que numberFloor dans la réponse est 1
                .andExpect(jsonPath("$.isForWomen").value(true)) // Vérifie que isForWomen dans la réponse est true
                .andExpect(jsonPath("$.idAfpaCenter.id").value(1)); // Vérifie que idAfpaCenter dans la réponse est 1
    }

    // Test pour la création d'un étage avec une exception de doublon
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testCreateFloor_DuplicateException() throws Exception {
        // Configuration du mock pour lancer une DuplicateException
        doThrow(new DuplicateException("Floor already exists")).when(floorService).create(any(FloorDTO.class));

        // Simulation d'une requête POST et vérification de la réponse pour l'exception
        mockMvc.perform(post("/api/floors/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"numberFloor\":1,\"isForWomen\":true,\"idAfpaCenter\":{\"id\":1}}") // Ajouter afpaCenter
                        .with(csrf()))
                .andExpect(status().isConflict()); // 409 Conflict
    }

    // Test pour la création d'un étage avec une exception de création
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testCreateFloor_CreationException() throws Exception {
        // Configuration du mock pour lancer une CreationException
        doThrow(new CreationException("Error occurred while creating floor")).when(floorService).create(any(FloorDTO.class));

        // Simulation d'une requête POST et vérification de la réponse pour l'exception
        mockMvc.perform(post("/api/floors/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"numberFloor\":1,\"isForWomen\":true,\"idAfpaCenter\":{\"id\":1}}") // Ajouter afpaCenter
                        .with(csrf()))
                .andExpect(status().isUnprocessableEntity()); // 422 Unprocessable Entity
    }


    // Test pour obtenir un étage par id
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetFloorById() throws Exception {
        // Création d'un AfpaCenterDTO pour le test
        AfpaCenterDTO afpaCenterDTO = new AfpaCenterDTO();
        afpaCenterDTO.setId(1);

        // Création d'un FloorDTO pour le test
        FloorDTO floorDTO = new FloorDTO();
        floorDTO.setId(1);
        floorDTO.setNumberFloor(1);
        floorDTO.setIsForWomen(true);
        floorDTO.setIdAfpaCenter(afpaCenterDTO);

        // Configuration du mock pour retourner un étage par ID
        when(floorService.getById(1)).thenReturn(Optional.of(floorDTO));

        // Simulation d'une requête GET avec ID et vérification de la réponse
        mockMvc.perform(get("/api/floors/1")) // Simulation d'une requête GET vers "/api/floors/1"
                .andExpect(status().isOk()) // Vérifie que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$.id").value(1)) // Vérifie que l'id de l'étage est 1
                .andExpect(jsonPath("$.numberFloor").value(1)) // Vérifie que numberFloor de l'étage est 1
                .andExpect(jsonPath("$.isForWomen").value(true)) // Vérifie que isForWomen de l'étage est true
                .andExpect(jsonPath("$.idAfpaCenter.id").value(1)); // Vérifie que idAfpaCenter de l'étage est 1
    }



    // Test pour obtenir un étage par id avec une exception de ressource non trouvée
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetFloorById_NotFoundException() throws Exception {
        // Configuration du mock pour lancer une ResourceNotFoundException
        when(floorService.getById(1)).thenThrow(new ResourceNotFoundException("Floor not found with id: 1"));

        // Simulation d'une requête GET avec ID et vérification de la réponse pour l'exception
        mockMvc.perform(get("/api/floors/1"))
                .andExpect(status().isNotFound()); // 404 Not Found
    }

    // Test pour mettre à jour un étage
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdateFloor() throws Exception {
        // Création d'un AfpaCenterDTO pour le test
        AfpaCenterDTO afpaCenterDTO = new AfpaCenterDTO();
        afpaCenterDTO.setId(1);

        // Création d'un FloorDTO pour le test
        FloorDTO floorDTO = new FloorDTO();
        floorDTO.setId(1);
        floorDTO.setNumberFloor(1);
        floorDTO.setIsForWomen(true);
        floorDTO.setIdAfpaCenter(afpaCenterDTO);

        // Configuration du mock pour retourner l'étage mis à jour
        when(floorService.update(any(Integer.class), any(FloorDTO.class))).thenReturn(Optional.of(floorDTO));

        // Simulation d'une requête PUT et vérification de la réponse
        mockMvc.perform(put("/api/floors/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"numberFloor\":1,\"isForWomen\":true,\"idAfpaCenter\":{\"id\":1}}") // Ajouter afpaCenter
                        .with(csrf()))
                .andExpect(status().isOk()) // 200 OK
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.numberFloor").value(1))
                .andExpect(jsonPath("$.isForWomen").value(true))
                .andExpect(jsonPath("$.idAfpaCenter.id").value(1));
    }


    // Test pour mettre à jour un étage avec une exception de ressource non trouvée
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdateFloor_NotFoundException() throws Exception {
        // Configuration du mock pour lancer une ResourceNotFoundException
        doThrow(new ResourceNotFoundException("Floor not found with id: 1"))
                .when(floorService).update(any(Integer.class), any(FloorDTO.class));

        // Simulation d'une requête PUT et vérification de la réponse pour l'exception
        mockMvc.perform(put("/api/floors/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"numberFloor\":1,\"isForWomen\":true,\"idAfpaCenter\":{\"id\":1}}") // Ajouter afpaCenter
                        .with(csrf()))
                .andExpect(status().isNotFound()); // 404 Not Found
    }


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDeleteFloor() throws Exception {
        // Configuration du mock pour ne rien faire (supprimer sans exception)
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/floors/delete/1")
                        .with(csrf()))
                .andExpect(status().isNoContent()); // 204 No Content
    }


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDeleteFloor_NotFoundException() throws Exception {
        // Configure le mock pour lancer une ResourceNotFoundException
        doThrow(new ResourceNotFoundException("Floor not found with id: 1"))
                .when(floorService).deleteById(1);

        // Simule une requête DELETE et vérifie la réponse pour l'exception
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/floors/delete/1")
                        .with(csrf()))
                .andExpect(status().isNotFound()); // 404 Not Found
    }

}
