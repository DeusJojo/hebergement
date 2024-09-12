package com.afpa.hebergement;

import com.afpa.hebergement.controller.AppUserController;
import com.afpa.hebergement.model.dto.*;
import com.afpa.hebergement.model.enum_role.RoleType;
import com.afpa.hebergement.service.entity_service.AppUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppUserController.class)
@ExtendWith(MockitoExtension.class)
public class AppUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppUserService appUserService;
    @Autowired
    private ObjectMapper jacksonObjectMapper;

    private AppUserControllerTest(){}

    private AppUserDTO createUserDTO() {

        // Création et définition de CivilityDTO
        CivilityDTO civilityDTO = new CivilityDTO();
        civilityDTO.setId(1);
        civilityDTO.setWordingCivility("Mr");

        // Création et définition de CityDTO pour AfpaCenter
        CityDTO afpaCenterCityDTO = new CityDTO();
        afpaCenterCityDTO.setId(2);
        afpaCenterCityDTO.setCityName("Lyon");
        afpaCenterCityDTO.setInseeCode("69389");
        afpaCenterCityDTO.setPostcode("69000");
        afpaCenterCityDTO.setIdDepartment(null); // Si DepartmentDTO n'est pas requis pour ce test

        // Création et définition de AfpaCenterDTO
        AfpaCenterDTO afpaCenterDTO = new AfpaCenterDTO();
        afpaCenterDTO.setId(1);
        afpaCenterDTO.setCenterName("Center A");
        afpaCenterDTO.setAddressCenter("456 Another St");
        afpaCenterDTO.setComplementAddrCenter("Suite 200");
        afpaCenterDTO.setSiren("123456789");
        afpaCenterDTO.setTvaNumber("FR123456789");
        afpaCenterDTO.setCenterPhoneNum("0123456789");
        afpaCenterDTO.setFaxCenter("0123456780");
        afpaCenterDTO.setIdCity(afpaCenterCityDTO);

        // Création et définition de CityDTO pour User
        CityDTO userCityDTO = new CityDTO();
        userCityDTO.setId(1);
        userCityDTO.setCityName("Paris");
        userCityDTO.setInseeCode("75056");
        userCityDTO.setPostcode("75000");
        userCityDTO.setIdDepartment(null); // Si DepartmentDTO n'est pas requis pour ce test

        // Création et définition de RoleDTO
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1);
        roleDTO.setWordingRole(RoleType.ROLE_ADMIN); // Supposant que RoleType est une énumération

        // Création et définition de ContactPersonDTO
        ContactPersonDTO contactPersonDTO = new ContactPersonDTO();
        contactPersonDTO.setId(1);
        contactPersonDTO.setName("John");
        contactPersonDTO.setFirstname("Doe");
        contactPersonDTO.setPhoneNumberContact("0987654321");

        // Création et définition de AppUserDTO
        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setId(1);
        appUserDTO.setBeneficiaryNumber("B123456");
        appUserDTO.setName("Doe");
        appUserDTO.setFirstname("John");
        appUserDTO.setEmail("testuser@example.com");
        appUserDTO.setPassword("password");
        appUserDTO.setBirthdate(LocalDate.of(1990, 5, 15));
        appUserDTO.setBirthplace("Paris");
        appUserDTO.setPhoneNumber("0123456789");
        appUserDTO.setIsBlacklisted(false);
        appUserDTO.setRegistrationDate(LocalDate.now());
        appUserDTO.setAddressUser("123 Main St");
        appUserDTO.setComplementUserAddress("Apt 101");
        appUserDTO.setIdRole(roleDTO);
        appUserDTO.setIdCivility(civilityDTO);
        appUserDTO.setIdCity(userCityDTO);
        appUserDTO.setIdAfpaCenter(afpaCenterDTO);
        appUserDTO.setIdContactPerson(contactPersonDTO);

        return appUserDTO;
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testCreateUser() throws Exception {
        AppUserDTO userDTO = createUserDTO();

        // Mock the service to return the created DTO
        when(appUserService.create(any(AppUserDTO.class))).thenReturn(userDTO);

        // Convert userDTO to JSON string with correctly formatted date
        String jsonRequest = jacksonObjectMapper.writeValueAsString(userDTO);

        // Execute POST request to create a user
        mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("testuser@example.com"))
                .andExpect(jsonPath("$.beneficiaryNumber").value("B123456"))
                .andExpect(jsonPath("$.name").value("Doe"))
                .andExpect(jsonPath("$.firstname").value("John"))
                .andExpect(jsonPath("$.birthdate").value("1990-05-15"))
                .andExpect(jsonPath("$.birthplace").value("Paris"))
                .andExpect(jsonPath("$.phoneNumber").value("0123456789"))
                .andExpect(jsonPath("$.isBlacklisted").value(false))
                .andExpect(jsonPath("$.registrationDate").value(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                .andExpect(jsonPath("$.ipRegistration").value("192.168.0.1"))
                .andExpect(jsonPath("$.trackerRegistration").value("Windows"))
                .andExpect(jsonPath("$.addressUser").value("123 Main St"))
                .andExpect(jsonPath("$.complementUserAddress").value("Apt 101"))
                .andExpect(jsonPath("$.idCivility.id").value(1))
                .andExpect(jsonPath("$.idRole.id").value(1))
                .andExpect(jsonPath("$.idCity.id").value(1))
                .andExpect(jsonPath("$.idAfpaCenter.id").value(1))
                .andExpect(jsonPath("$.idContactPerson.id").value(1));

        // Verify that the service method was called with the correct DTO
        verify(appUserService).create(any(AppUserDTO.class));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetAllUsers() throws Exception {
        // Création d'un utilisateur DTO pour le test
        AppUserDTO userDTO = createUserDTO();

        // Configuration du mock pour retourner une liste d'utilisateurs
        when(appUserService.getAll()).thenReturn(List.of(userDTO));

        // Effectuer une requête GET et vérifier la réponse
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].email").value("testuser@example.com"))
                .andExpect(jsonPath("$[0].beneficiaryNumber").value("B123456"))
                .andExpect(jsonPath("$[0].name").value("Doe"))
                .andExpect(jsonPath("$[0].firstname").value("John"))
                .andExpect(jsonPath("$[0].birthdate").value("1990-05-15"))
                .andExpect(jsonPath("$[0].birthplace").value("Paris"))
                .andExpect(jsonPath("$[0].phoneNumber").value("0123456789"))
                .andExpect(jsonPath("$[0].isBlacklisted").value(false))
                .andExpect(jsonPath("$[0].registrationDate").value(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                .andExpect(jsonPath("$[0].ipRegistration").value("192.168.0.1"))
                .andExpect(jsonPath("$[0].trackerRegistration").value("Windows"))
                .andExpect(jsonPath("$[0].addressUser").value("123 Main St"))
                .andExpect(jsonPath("$[0].complementUserAddress").value("Apt 101"))
                .andExpect(jsonPath("$[0].idCivility.id").value(1))
                .andExpect(jsonPath("$[0].idRole.id").value(1))
                .andExpect(jsonPath("$[0].idCity.id").value(1))
                .andExpect(jsonPath("$[0].idAfpaCenter.id").value(1))
                .andExpect(jsonPath("$[0].idContactPerson.id").value(1));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetAllUsersByAfpaCenter() throws Exception {
        // Création d'un utilisateur DTO pour le test
        AppUserDTO userDTO = createUserDTO();

        // ID du centre Afpa pour le test
        int idAfpaCenter = 1;

        // Configuration du mock pour retourner une liste d'utilisateurs pour le centre Afpa avec l'ID spécifié
        when(appUserService.getAllByAfpaCenter(idAfpaCenter)).thenReturn(List.of(userDTO));

        // Effectuer une requête GET pour récupérer tous les utilisateurs pour le centre Afpa avec l'ID 1
        mockMvc.perform(get("/api/users/afpa-center/{idAfpaCenter}", idAfpaCenter))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].email").value("testuser@example.com"))
                .andExpect(jsonPath("$[0].beneficiaryNumber").value("B123456"))
                .andExpect(jsonPath("$[0].name").value("Doe"))
                .andExpect(jsonPath("$[0].firstname").value("John"))
                .andExpect(jsonPath("$[0].birthdate").value("1990-05-15"))
                .andExpect(jsonPath("$[0].birthplace").value("Paris"))
                .andExpect(jsonPath("$[0].phoneNumber").value("0123456789"))
                .andExpect(jsonPath("$[0].isBlacklisted").value(false))
                .andExpect(jsonPath("$[0].registrationDate").value(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                .andExpect(jsonPath("$[0].ipRegistration").value("192.168.0.1"))
                .andExpect(jsonPath("$[0].trackerRegistration").value("Windows"))
                .andExpect(jsonPath("$[0].addressUser").value("123 Main St"))
                .andExpect(jsonPath("$[0].complementUserAddress").value("Apt 101"))
                .andExpect(jsonPath("$[0].idCivility.id").value(1))
                .andExpect(jsonPath("$[0].idRole.id").value(1))
                .andExpect(jsonPath("$[0].idCity.id").value(1))
                .andExpect(jsonPath("$[0].idAfpaCenter.id").value(1))
                .andExpect(jsonPath("$[0].idContactPerson.id").value(1));

        // Vérifier que la méthode de service a été appelée avec le bon ID de centre Afpa
        verify(appUserService).getAllByAfpaCenter(idAfpaCenter);
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetUserById() throws Exception {
        // Création d'un utilisateur DTO pour le test
        AppUserDTO userDTO = createUserDTO();

        // Configuration du mock pour retourner le DTO utilisateur lors de l'appel à getById avec l'ID 1
        when(appUserService.getById(1)).thenReturn(Optional.of(userDTO));

        // Effectuer une requête GET avec l'ID 1 et vérifier la réponse
        mockMvc.perform(get("/api/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("testuser@example.com"))
                .andExpect(jsonPath("$.beneficiaryNumber").value("B123456"))
                .andExpect(jsonPath("$.name").value("Doe"))
                .andExpect(jsonPath("$.firstname").value("John"))
                .andExpect(jsonPath("$.birthdate").value("1990-05-15"))
                .andExpect(jsonPath("$.birthplace").value("Paris"))
                .andExpect(jsonPath("$.phoneNumber").value("0123456789"))
                .andExpect(jsonPath("$.isBlacklisted").value(false))
                .andExpect(jsonPath("$.registrationDate").value(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                .andExpect(jsonPath("$.ipRegistration").value("192.168.0.1"))
                .andExpect(jsonPath("$.trackerRegistration").value("Windows"))
                .andExpect(jsonPath("$.addressUser").value("123 Main St"))
                .andExpect(jsonPath("$.complementUserAddress").value("Apt 101"))
                .andExpect(jsonPath("$.idCivility.id").value(1))
                .andExpect(jsonPath("$.idRole.id").value(1))
                .andExpect(jsonPath("$.idCity.id").value(1))
                .andExpect(jsonPath("$.idAfpaCenter.id").value(1))
                .andExpect(jsonPath("$.idContactPerson.id").value(1));
    }


    @Test
    @WithMockUser(username = "user", roles = {"USERS"})
    public void testUpdateUser() throws Exception {
        // Création d'un utilisateur DTO pour le test
        AppUserDTO updatedUserDTO = createUserDTO();

        updatedUserDTO.setEmail("updateduser@example.com");

        updatedUserDTO.setName("Doe");
        updatedUserDTO.setFirstname("John");
        updatedUserDTO.setBirthdate(LocalDate.of(1990, 5, 15));
        updatedUserDTO.setBirthplace("Paris");
        updatedUserDTO.setPhoneNumber("0123456789");
        updatedUserDTO.setIsBlacklisted(false);
        updatedUserDTO.setAddressUser("123 Main St");
        updatedUserDTO.setComplementUserAddress("Apt 101");

        CivilityDTO updatedCivilityDTO = new CivilityDTO();
        updatedCivilityDTO.setId(1);
        updatedUserDTO.setIdCivility(updatedCivilityDTO);

        CityDTO updatedCityDTO = new CityDTO();
        updatedCityDTO.setId(1);
        updatedUserDTO.setIdCity(updatedCityDTO);

        ContactPersonDTO updatedContactPersonDTO = new ContactPersonDTO();
        updatedContactPersonDTO.setId(1);
        updatedUserDTO.setIdContactPerson(updatedContactPersonDTO);

        RoleDTO updatedRoleDTO = new RoleDTO();
        updatedRoleDTO.setId(1);
        updatedUserDTO.setIdRole(updatedRoleDTO);

        AfpaCenterDTO updatedAfpaCenterDTO = new AfpaCenterDTO();
        updatedAfpaCenterDTO.setId(1);
        updatedUserDTO.setIdAfpaCenter(updatedAfpaCenterDTO);

        // Mock du service pour retourner l'utilisateur mis à jour
        when(appUserService.update(any(Integer.class), any(AppUserDTO.class))).thenReturn(Optional.of(updatedUserDTO));

        // Conversion du DTO mis à jour en JSON
        String updatedUserJson = jacksonObjectMapper.writeValueAsString(updatedUserDTO);

        // Effectuer une requête PUT pour mettre à jour l'utilisateur avec l'ID 1
        mockMvc.perform(put("/api/users/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(updatedUserDTO.getEmail()))
                .andExpect(jsonPath("$.name").value(updatedUserDTO.getName()))
                .andExpect(jsonPath("$.firstname").value(updatedUserDTO.getFirstname()))
                .andExpect(jsonPath("$.birthdate").value("1990-05-15"))
                .andExpect(jsonPath("$.birthplace").value(updatedUserDTO.getBirthplace()))
                .andExpect(jsonPath("$.phoneNumber").value(updatedUserDTO.getPhoneNumber()))
                .andExpect(jsonPath("$.isBlacklisted").value(false))
                .andExpect(jsonPath("$.addressUser").value(updatedUserDTO.getAddressUser()))
                .andExpect(jsonPath("$.complementUserAddress").value(updatedUserDTO.getComplementUserAddress()))
                .andExpect(jsonPath("$.idCivility.id").value(updatedUserDTO.getIdCivility().getId()))
                .andExpect(jsonPath("$.idRole.id").value(updatedUserDTO.getIdRole().getId()))
                .andExpect(jsonPath("$.idCity.id").value(updatedUserDTO.getIdCity().getId()))
                .andExpect(jsonPath("$.idAfpaCenter.id").value(updatedUserDTO.getIdAfpaCenter().getId()))
                .andExpect(jsonPath("$.idContactPerson.id").value(updatedUserDTO.getIdContactPerson().getId()));

        // Vérifier que la méthode de service a été appelée avec le bon DTO utilisateur
        verify(appUserService).update(any(Integer.class), any(AppUserDTO.class));
    }



    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdateUserById() throws Exception {
        // Création d'un utilisateur initial
        UserDTO initialUserDTO = new UserDTO();
        initialUserDTO.setEmail("initial@example.com");
        initialUserDTO.setPhoneNumber("0987654321");
        initialUserDTO.setAddressUser("Initial Street");
        initialUserDTO.setComplementUserAddress("Initial Apt");

        CivilityDTO initialCivilityDTO = new CivilityDTO();
        initialCivilityDTO.setId(1); // ID fictif pour civility
        initialUserDTO.setIdCivility(initialCivilityDTO);

        CityDTO initialCityDTO = new CityDTO();
        initialCityDTO.setId(1); // ID fictif pour city
        initialUserDTO.setIdCity(initialCityDTO);

        ContactPersonDTO initialContactPersonDTO = new ContactPersonDTO();
        initialContactPersonDTO.setId(1); // ID fictif pour contact person
        initialUserDTO.setIdContactPerson(initialContactPersonDTO);

        // Mock du service pour retourner l'utilisateur initial
        when(appUserService.updateUser(any(Integer.class), any(UserDTO.class))).thenReturn(Optional.of(initialUserDTO));

        // Mise à jour des données de l'utilisateur
        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setEmail("updated@example.com");
        updatedUserDTO.setPhoneNumber("1234567890");
        updatedUserDTO.setAddressUser("Updated Street");
        updatedUserDTO.setComplementUserAddress("Updated Apt");

        CivilityDTO updatedCivilityDTO = new CivilityDTO();
        updatedCivilityDTO.setId(2); // Nouveau ID pour civility
        updatedUserDTO.setIdCivility(updatedCivilityDTO);

        CityDTO updatedCityDTO = new CityDTO();
        updatedCityDTO.setId(2); // Nouveau ID pour city
        updatedUserDTO.setIdCity(updatedCityDTO);

        ContactPersonDTO updatedContactPersonDTO = new ContactPersonDTO();
        updatedContactPersonDTO.setId(2); // Nouveau ID pour contact person
        updatedUserDTO.setIdContactPerson(updatedContactPersonDTO);

        // Mock du service pour retourner l'utilisateur mis à jour
        when(appUserService.updateUser(any(Integer.class), any(UserDTO.class))).thenReturn(Optional.of(updatedUserDTO));

        // Conversion du DTO mis à jour en JSON
        String updatedUserJson = jacksonObjectMapper.writeValueAsString(updatedUserDTO);

        // Effectuer une requête PUT pour mettre à jour l'utilisateur avec l'ID 1
        mockMvc.perform(put("/api/users/update/guest/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(updatedUserDTO.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(updatedUserDTO.getPhoneNumber()))
                .andExpect(jsonPath("$.addressUser").value(updatedUserDTO.getAddressUser()))
                .andExpect(jsonPath("$.complementUserAddress").value(updatedUserDTO.getComplementUserAddress()))
                .andExpect(jsonPath("$.idCivility.id").value(updatedUserDTO.getIdCivility().getId()))
                .andExpect(jsonPath("$.idCity.id").value(updatedUserDTO.getIdCity().getId()))
                .andExpect(jsonPath("$.idContactPerson.id").value(updatedUserDTO.getIdContactPerson().getId()));

        // Vérifier que la méthode de service a été appelée avec le bon ID d'utilisateur et le DTO correspondant
        verify(appUserService).updateUser(any(Integer.class), any(UserDTO.class));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testDeleteUser() throws Exception {
        doNothing().when(appUserService).deleteById(1);

        mockMvc.perform(delete("/api/users/delete/{id}", 1)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(appUserService, times(1)).deleteById(1);
    }
}
