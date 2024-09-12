package com.afpa.hebergement;

import com.afpa.hebergement.controller.RoleController;
import com.afpa.hebergement.exception.CreationException;
import com.afpa.hebergement.exception.DuplicateException;
import com.afpa.hebergement.exception.NoContentException;
import com.afpa.hebergement.exception.ResourceNotFoundException;
import com.afpa.hebergement.model.dto.RoleDTO;
import com.afpa.hebergement.model.enum_role.RoleType;
import com.afpa.hebergement.service.entity_service.RoleService;
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



@WebMvcTest(RoleController.class) // Tester le contrôleur RoleController
@ExtendWith(MockitoExtension.class) // Permet l'utilisation de Mockito dans ce test
public class RoleControllerTest {


    @Autowired
    private MockMvc mockMvc; // Injecte le MockMvc pour simuler les requêtes HTTP
    @MockBean
    private RoleService roleService; // Crée un mock du service RoleService pour simuler les interactions avec la base de données


    //test create Role
    @Test // Indique que cette méthode est un test unitaire
    @WithMockUser(username = "user", roles = {"USER"}) // Simule un utilisateur authentifié avec le rôle "USER"
    public void testCreateRole() throws Exception { // Déclare une méthode de test qui peut lever des exceptions

        // Crée un DTO Role pour le test
        RoleDTO roleDTO = new RoleDTO(); // Instancie un nouvel objet RoleDTO
        roleDTO.setId(1); // Définit l'ID du rôle à 1
        roleDTO.setWordingRole(RoleType.ROLE_ADMIN); // Définit le libellé du rôle à "ROLE_ADMIN"

        // Configure le mock pour retourner le DTO Role lors de l'appel create
        when(roleService.create(any(RoleDTO.class))).thenReturn(roleDTO); // Simule le comportement de la méthode create du service pour retourner le roleDTO

        // Simule une requête POST et vérifie la réponse
        // mockMvc: instance de MockMvc utilisée pour simuler les requêtes HTTP.
        // perform: méthode qui lance l'exécution d'une requête HTTP simulée.
        mockMvc.perform(post("/api/roles/create") // Simule une requête POST à l'URL "/api/roles/create"
                        .contentType(MediaType.APPLICATION_JSON) // Indique que le contenu de la requête est en JSON
                        .content("{\"id\":1,\"wordingRole\":\"ROLE_ADMIN\"}") // Fournit le corps de la requête en JSON
                        .with(csrf())) // Ajoute un jeton CSRF valide à la requête
                .andExpect(status().isCreated()) // Vérifie que le statut de la réponse est 201 (Created)
                .andExpect(jsonPath("$.id").value(1)) // Vérifie que l'ID du rôle dans la réponse est 1, Le symbole $ représente l'objet racine dans la réponse JSON et accede au champs id
                .andExpect(jsonPath("$.wordingRole").value("ROLE_ADMIN")); // Vérifie que le libellé du rôle dans la réponse est "ROLE_ADMIN"
    }


    //test create role avec duplicate exception
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testCreateRole_DuplicateException() throws Exception {

        // Configure le mock pour lancer une DuplicateException
        // doThrow : utilisée pour spécifier que lorsque la méthode suivante est appelée sur le mock, une DuplicateException doit être lancée.
        // when: spécifie le comportement configuré (doThrow) doit se produire lorsque la méthode create est appelée sur le mock roleService avec n'importe quel objet de type RoleDTO en argument.
        doThrow(new DuplicateException("Role already exists")).when(roleService).create(any(RoleDTO.class));

        // Simule une requête POST et vérifie la réponse pour l'exception
        mockMvc.perform(post("/api/roles/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"wordingRole\":\"ROLE_ADMIN\"}")
                        .with(csrf()))
                .andExpect(status().isConflict()); // 409 Conflict
    }


    //test create role avec exception
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testCreateRole_CreationException() throws Exception {

        // Configure le mock pour lancer une CreationException
        doThrow(new CreationException("Error occurred while creating role")).when(roleService).create(any(RoleDTO.class));

        // Simule une requête POST et vérifie la réponse pour l'exception
        mockMvc.perform(post("/api/roles/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"wordingRole\":\"ROLE_ADMIN\"}")
                        .with(csrf()))
                .andExpect(status().isUnprocessableEntity()); // statut 422 (Unprocessable Entity)
    }


    //test get all roles
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAllRoles() throws Exception {
        // Crée un DTO Role pour le test
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1);
        roleDTO.setWordingRole(RoleType.ROLE_ADMIN);

        // Configure le mock pour retourner une liste de rôles
        // Collections.singletonList: créer une liste immuable contenant un seul élément 'roleDTO'
        when(roleService.getAll()).thenReturn(Collections.singletonList(roleDTO));

        // Simule une requête GET et vérifie la réponse
        mockMvc.perform(get("/api/roles")) // Simule une requête GET à l'endpoint /api/roles
                .andExpect(status().isOk()) // Vérifie que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$[0].id").value(1)) // Vérifie que l'id du premier role ([0]) est 1
                .andExpect(jsonPath("$[0].wordingRole").value("ROLE_ADMIN")); // Vérifie que wordingRole du premier Role est "ROLE_ADMIN"
    }


    //test get all roles avec exception
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAllRoles_NoContentException() throws Exception {

        // Configure le mock pour lancer une NoContentException
        doThrow(new NoContentException("No roles found")).when(roleService).getAll();

        // Simule une requête GET et vérifie la réponse pour l'exception
        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isNoContent()); // 204 No Content
    }


    //test get role by id
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetRoleById() throws Exception {
        // Crée un DTO Role pour le test
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1);
        roleDTO.setWordingRole(RoleType.ROLE_ADMIN);

        // Configure le mock pour retourner un rôle par ID
        when(roleService.getById(1)).thenReturn(Optional.of(roleDTO));

        // Simule une requête GET avec ID et vérifie la réponse
        mockMvc.perform(get("/api/roles/1")) // Simule une requête GET à l'endpoint /api/roles/{id}
                .andExpect(status().isOk()) // Vérifie que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$.id").value(1)) // Vérifie que l'id du role est 1
                .andExpect(jsonPath("$.wordingRole").value("ROLE_ADMIN")); // Vérifie que wordingRole de Role est "ROLE_ADMIN"
    }


    //test get role by id avec exception
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetRoleById_NotFoundException() throws Exception {
        // Configure le mock pour lancer une ResourceNotFoundException
        when(roleService.getById(1)).thenThrow(new ResourceNotFoundException("Role not found with id: 1"));

        // Simule une requête GET avec ID et vérifie la réponse pour l'exception
        mockMvc.perform(get("/api/roles/1"))
                .andExpect(status().isNotFound()); // 404 Not Found
    }


    //test update role
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdateRole() throws Exception {
        // Create a RoleDTO for the test
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1);
        roleDTO.setWordingRole(RoleType.ROLE_ADMIN);

        // Configure the mock to return an updated role
        when(roleService.update(any(Integer.class), any(RoleDTO.class))).thenReturn(Optional.of(roleDTO));

        // Simulate a PUT request and verify the response
        mockMvc.perform(put("/api/roles/update/1") // Simule une requête PUT à l'endpoint /api/roles/update/1
                        .contentType(MediaType.APPLICATION_JSON) // Indique que le contenu est en JSON
                        .content("{\"id\":1,\"wordingRole\":\"ROLE_ADMIN\"}") // Contenu de la requête en JSON
                        .with(csrf())) // ajout d'un CSRF token
                .andExpect(status().isOk()) // Vérifie que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$.id").value(1)) // Vérifie que l'id de la réponse est 1
                .andExpect(jsonPath("$.wordingRole").value("ROLE_ADMIN")); // Vérifie que wordingRole de la réponse est "ROLE_ADMIN"
    }


    //test update role avec exception
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdateRole_NotFoundException() throws Exception {

        // Configure le mock pour lancer une ResourceNotFoundException
        when(roleService.update(any(Integer.class), any(RoleDTO.class))).thenThrow(new ResourceNotFoundException("Role not found with id: 1"));

        // Simule une requête PUT et vérifie la réponse pour l'exception
        mockMvc.perform(put("/api/roles/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"wordingRole\":\"ROLE_ADMIN\"}")
                        .with(csrf()))
                .andExpect(status().isNotFound()); // 404 Not Found
    }


    //test delete role by id
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDeleteRole() throws Exception {

        // Simule une requête DELETE et vérifie la réponse
        mockMvc.perform(delete("/api/roles/delete/1") // Simule une requête DELETE à l'endpoint /api/roles/delete/1
                        .with(csrf())) // Ajoute un jeton CSRF valide
                .andExpect(status().isNoContent());  // Vérifie que le statut de la réponse est 204 (No Content)
    }


    //test delete role by id avec exception
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDeleteRole_NotFoundException() throws Exception {

        // Configure le mock pour lancer une ResourceNotFoundException
        doThrow(new ResourceNotFoundException("Role not found with id: 1")).when(roleService).deleteById(1);

        // Simule une requête DELETE et vérifie la réponse pour l'exception
        mockMvc.perform(delete("/api/roles/delete/1")
                        .with(csrf()))
                .andExpect(status().isNotFound()); // 404 Not Found
    }

}




