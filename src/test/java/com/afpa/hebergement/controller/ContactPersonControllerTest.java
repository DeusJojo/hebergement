package com.afpa.hebergement.controller;

import com.afpa.hebergement.model.dto.ContactPersonDTO;
import com.afpa.hebergement.security.WebSecurityConfig;
import com.afpa.hebergement.service.entity_service.ContactPersonService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ContactPersonController.class)
@Import(WebSecurityConfig.class) // Importer la configuration de sécurité dans le contexte de test
class ContactPersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactPersonService contactPersonService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreate() throws Exception {
        // Prépare un objet ContactPersonDto avec des données spécifiques pour le test
        ContactPersonDTO contactPersonDTO = new ContactPersonDTO();
        contactPersonDTO.setName("Doe");
        contactPersonDTO.setFirstname("Jojo");
        contactPersonDTO.setPhoneNumberContact("0214584789");

        // Simuler la réponse de la méthode create du service
        ContactPersonDTO savedContactPersonDTO = new ContactPersonDTO();
        savedContactPersonDTO.setId(1);
        savedContactPersonDTO.setName("Doe");
        savedContactPersonDTO.setFirstname("Jojo");
        savedContactPersonDTO.setPhoneNumberContact("0214584789");

        when(contactPersonService.create(any(ContactPersonDTO.class))).thenReturn(savedContactPersonDTO);

        // Effectue une requête POST vers /contactPerson/create avec l'objet contactPersonDTO converti en JSON
        mockMvc.perform(post("/api/contactPerson/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contactPersonDTO)))

                // Vérifie que la réponse HTTP a un statut 201 (Created)
                .andExpect(status().isCreated())

                // Vérifie que la réponse JSON contient les attributs attendus
                .andExpect((jsonPath("$.id", is(1))))
                .andExpect((jsonPath("$.name", is("Doe"))))
                .andExpect((jsonPath("$.phoneNumberContact", is("0214584789"))))
                .andExpect((jsonPath("$.firstname", is("Jojo"))));

    }

    @Test
    void testGetById() throws Exception {
        ContactPersonDTO contactPersonDTO = new ContactPersonDTO();
        contactPersonDTO.setId(1);
        contactPersonDTO.setName("Doe");
        contactPersonDTO.setFirstname("Jojo");
        contactPersonDTO.setPhoneNumberContact("0214584789");

        // Simuler la réponse de la méthode getById du service
        when(contactPersonService.getById(1)).thenReturn(Optional.of(contactPersonDTO));

        // Effectue une requête GET vers /contactPerson/getById avec le paramètre id=1
        mockMvc.perform(get("/api/contactPerson/{id}", 1)
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                // Vérifie que la réponse HTTP a un statut 200 (OK)
                .andExpect(status().isOk())
                // Vérifie que la réponse JSON contient les attributs attendus
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Doe")))
                .andExpect(jsonPath("$.firstname", is("Jojo")))
                .andExpect(jsonPath("$.phoneNumberContact", is("0214584789")));
    }

    @Test
    void testGetByIdNotFound() throws Exception {
        // Simuler une réponse vide pour un ID qui n'existe pas
        when(contactPersonService.getById(2)).thenReturn(Optional.empty());

        // Effectue une requête GET vers /contactPerson/getById avec le paramètre id=2
        mockMvc.perform(get("/api/contactPerson/{id}", 2)
                        .param("id", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                // Vérifie que la réponse HTTP a un statut 404 (Not Found)
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdate() throws Exception {
        // Crée un DTO ContactPersonDTO pour le test
        ContactPersonDTO contactPersonDTO = new ContactPersonDTO();
        contactPersonDTO.setId(1);
        contactPersonDTO.setName("Doe");
        contactPersonDTO.setFirstname("Jojo");
        contactPersonDTO.setPhoneNumberContact("0214584789");

        // Configure le mock pour retourner une ContactPersonDTO mise à jour
        when(contactPersonService.update(any(Integer.class), any(ContactPersonDTO.class)))
                .thenReturn(Optional.of(contactPersonDTO));

        String jsonRequest = objectMapper.writeValueAsString(contactPersonDTO);

        mockMvc.perform(put("/api/contactPerson/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Doe")))
                .andExpect(jsonPath("$.firstname", is("Jojo")))
                .andExpect(jsonPath("$.phoneNumberContact", is("0214584789")));

    }

    @Test
    void testDelete() throws Exception {
        int contactPersonId = 1;

        mockMvc.perform(delete("/api/contactPerson/delete/{id}", contactPersonId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}