package com.afpa.hebergement;

import com.afpa.hebergement.controller.CivilityController;
import com.afpa.hebergement.exception.CreationException;
import com.afpa.hebergement.exception.DuplicateException;
import com.afpa.hebergement.exception.NoContentException;
import com.afpa.hebergement.exception.ResourceNotFoundException;
import com.afpa.hebergement.model.dto.CivilityDTO;
import com.afpa.hebergement.service.entity_service.CivilityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(CivilityController.class) // Tester le contrôleur CivilityController
@ExtendWith(MockitoExtension.class) // Permet l'utilisation de Mockito dans ce test
public class CivilityControllerTest {


    @Autowired // Injecte le MockMvc pour simuler les requêtes HTTP
    private MockMvc mockMvc;

    @MockBean // Crée un mock du service CivilityService pour simuler les interactions avec la base de données
    private CivilityService civilityService;


    @Test // Indique que cette méthode est un test unitaire
    @WithMockUser(username = "user", roles = {"USER"}) // Simule un utilisateur avec le rôle USER
    public void testCreateCivility() throws Exception {
        // Crée un DTO Civility pour le test
        CivilityDTO civilityDTO = new CivilityDTO();
        civilityDTO.setId(1);
        civilityDTO.setWordingCivility("Monsieur");

        // Configure le mock pour retourner le DTO Civility lors de l'appel create
        when(civilityService.create(any(CivilityDTO.class))).thenReturn(civilityDTO);

        // Simule une requête POST et vérifie la réponse
        mockMvc.perform(MockMvcRequestBuilders.post("/api/civilities/create") // Simule une requête POST à l'endpoint /api/civilities/create
                        .contentType(MediaType.APPLICATION_JSON) // Indique que le contenu est en JSON
                        .content("{\"id\":1,\"wordingCivility\":\"Monsieur\"}") // Contenu de la requête en JSON
                        .with(csrf())) // Ajoute un jeton CSRF valide pour la requête
                .andExpect(status().isCreated()) // Vérifie que le statut de la réponse est 201 (Created)
                .andExpect(jsonPath("$.id").value(1)) // Vérifie que l'id de la réponse est 1
                .andExpect(jsonPath("$.wordingCivility").value("Monsieur")); // Vérifie que wordingCivility de la réponse est "Monsieur"
    }


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testCreateCivility_DuplicateException() throws Exception {
        // Configure le mock pour lancer une DuplicateException
        doThrow(new DuplicateException("Civility already exists")).when(civilityService).create(any(CivilityDTO.class));

        // Simule une requête POST et vérifie la réponse pour l'exception
        mockMvc.perform(MockMvcRequestBuilders.post("/api/civilities/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"wordingCivility\":\"Monsieur\"}")
                        .with(csrf()))
                .andExpect(status().isConflict()); // 409 Conflict
    }


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testCreateCivility_CreationException() throws Exception {
        // Configure le mock pour lancer une CreationException
        doThrow(new CreationException("Error occurred while creating civility")).when(civilityService).create(any(CivilityDTO.class));

        // Simule une requête POST et vérifie la réponse pour l'exception
        mockMvc.perform(MockMvcRequestBuilders.post("/api/civilities/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"wordingCivility\":\"Monsieur\"}")
                        .with(csrf()))
                .andExpect(status().isUnprocessableEntity()); // 422 (Unprocessable Entity)
    }


    @Test // Indique que cette méthode est un test unitaire
    @WithMockUser(username = "user", roles = {"USER"}) // Simule un utilisateur avec le rôle USER
    public void testGetAllCivilities() throws Exception {
        // Crée un DTO Civility pour le test
        CivilityDTO civilityDTO = new CivilityDTO();
        civilityDTO.setId(1);
        civilityDTO.setWordingCivility("Monsieur");

        // Configure le mock pour retourner une liste de civilités
        when(civilityService.getAll()).thenReturn(Collections.singletonList(civilityDTO));

        // Simule une requête GET et vérifie la réponse
        mockMvc.perform(MockMvcRequestBuilders.get("/api/civilities")) // Simule une requête GET à l'endpoint /api/civilities
                .andExpect(status().isOk()) // Vérifie que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$[0].id").value(1)) // Vérifie que l'id de la première civilité est 1
                .andExpect(jsonPath("$[0].wordingCivility").value("Monsieur")); // Vérifie que wordingCivility de la première civilité est "Monsieur"
    }


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAllCivilities_NoContentException() throws Exception {
        // Configure le mock pour lancer une NoContentException
        doThrow(new NoContentException("No civilities found")).when(civilityService).getAll();

        // Simule une requête GET et vérifie la réponse pour l'exception
        mockMvc.perform(MockMvcRequestBuilders.get("/api/civilities"))
                .andExpect(status().isNoContent()); // 204 No Content
    }


    @Test // Indique que cette méthode est un test unitaire
    @WithMockUser(username = "user", roles = {"USER"}) // Simule un utilisateur avec le rôle USER
    public void testGetCivilityById() throws Exception {
        // Crée un DTO Civility pour le test
        CivilityDTO civilityDTO = new CivilityDTO();
        civilityDTO.setId(1);
        civilityDTO.setWordingCivility("Monsieur");

        // Configure le mock pour retourner une civilité par ID
        when(civilityService.getById(1)).thenReturn(Optional.of(civilityDTO));

        // Simule une requête GET avec ID et vérifie la réponse
        mockMvc.perform(MockMvcRequestBuilders.get("/api/civilities/1")) // Simule une requête GET à l'endpoint /api/civilities/1
                .andExpect(status().isOk()) // Vérifie que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$.id").value(1)) // Vérifie que l'id de la civilité est 1
                .andExpect(jsonPath("$.wordingCivility").value("Monsieur")); // Vérifie que wordingCivility de la civilité est "Monsieur"
    }


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetCivilityById_NotFoundException() throws Exception {
        // Configure le mock pour lancer une ResourceNotFoundException
        when(civilityService.getById(1)).thenThrow(new ResourceNotFoundException("Civility not found with id: 1"));

        // Simule une requête GET avec ID et vérifie la réponse pour l'exception
        mockMvc.perform(MockMvcRequestBuilders.get("/api/civilities/1"))
                .andExpect(status().isNotFound()); // 404 Not Found
    }


    @Test // Indique que cette méthode est un test unitaire
    @WithMockUser(username = "user", roles = {"USER"}) // Simule un utilisateur avec le rôle USER
    public void testUpdateCivility() throws Exception {
        // Crée un DTO Civility pour le test
        CivilityDTO civilityDTO = new CivilityDTO();
        civilityDTO.setId(1);
        civilityDTO.setWordingCivility("Monsieur");

        // Configure le mock pour retourner une civilité mise à jour
        when(civilityService.update(any(Integer.class), any(CivilityDTO.class))).thenReturn(Optional.of(civilityDTO));

        // Simule une requête PUT et vérifie la réponse
        mockMvc.perform(MockMvcRequestBuilders.put("/api/civilities/update/1") // Simule une requête PUT à l'endpoint /api/civilities/update/1
                        .contentType(MediaType.APPLICATION_JSON) // Indique que le contenu est en JSON
                        .content("{\"id\":1,\"wordingCivility\":\"Monsieur\"}") // Contenu de la requête en JSON
                        .with(csrf())) // Ajoute un jeton CSRF valide pour la requête
                .andExpect(status().isOk()) // Vérifie que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$.id").value(1)) // Vérifie que l'id de la réponse est 1
                .andExpect(jsonPath("$.wordingCivility").value("Monsieur")); // Vérifie que wordingCivility de la réponse est "Monsieur"
    }


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdateCivility_NotFoundException() throws Exception {
        // Configure le mock pour lancer une ResourceNotFoundException
        when(civilityService.update(any(Integer.class), any(CivilityDTO.class))).thenThrow(new ResourceNotFoundException("Civility not found with id: 1"));

        // Simule une requête PUT et vérifie la réponse pour l'exception
        mockMvc.perform(MockMvcRequestBuilders.put("/api/civilities/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"wordingCivility\":\"Monsieur\"}")
                        .with(csrf()))
                .andExpect(status().isNotFound()); // 404 Not Found
    }


    @Test // Indique que cette méthode est un test unitaire
    @WithMockUser(username = "user", roles = {"USER"}) // Simule un utilisateur avec le rôle USER
    public void testDeleteCivility() throws Exception {
        // Simule une requête DELETE et vérifie la réponse
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/civilities/delete/1") // Simule une requête DELETE à l'endpoint /api/civilities/delete/1
                        .with(csrf())) // Ajoute un jeton CSRF valide pour la requête
                .andExpect(status().isNoContent()); // Vérifie que le statut de la réponse est 204 (No Content)
    }


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDeleteCivility_NotFoundException() throws Exception {
        // Configure le mock pour lancer une ResourceNotFoundException
        doThrow(new ResourceNotFoundException("Civility not found with id: 1")).when(civilityService).deleteById(1);

        // Simule une requête DELETE et vérifie la réponse pour l'exception
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/civilities/delete/1")
                        .with(csrf()))
                .andExpect(status().isNotFound()); // 404 Not Found
    }
}

