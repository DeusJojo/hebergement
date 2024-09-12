package com.afpa.hebergement;

import com.afpa.hebergement.controller.RoomController;
import com.afpa.hebergement.exception.*;
import com.afpa.hebergement.model.dto.AfpaCenterDTO;
import com.afpa.hebergement.model.dto.FloorDTO;
import com.afpa.hebergement.model.dto.RoomDTO;
import com.afpa.hebergement.service.entity_service.RoomService;
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

@WebMvcTest(RoomController.class) // Tester le contrôleur RoomController
@ExtendWith(MockitoExtension.class) // Utilisation de Mockito pour le test
public class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc; // MockMvc pour simuler les requêtes HTTP

    @MockBean
    private RoomService roomService; // Mock du service RoomService pour simuler les interactions avec la base de données


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testCreateRoom() throws Exception {

        // Préparation des DTO pour le test
        AfpaCenterDTO afpaCenter = new AfpaCenterDTO();
        afpaCenter.setId(1);
        afpaCenter.setCenterName("Center 1");

        FloorDTO floor = new FloorDTO();
        floor.setId(1);
        floor.setNumberFloor(1);
        floor.setIsForWomen(true);

        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(1);
        roomDTO.setRoomNumber("101");
        roomDTO.setRoomKeyNumber(202);
        roomDTO.setIsUsable(true);
        roomDTO.setBadgeNumber(303);
        roomDTO.setFridgeKeyNumber(404);
        roomDTO.setIsReserved(false);
        roomDTO.setIdFloor(floor);


        // Configuration du mock pour retourner le RoomDTO lors de l'appel de la méthode create
        when(roomService.create(any(RoomDTO.class))).thenReturn(roomDTO);

        // Effectuer une requête POST et vérifier la réponse
        mockMvc.perform(post("/api/rooms/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"roomNumber\":101,\"roomKeyNumber\":202,\"isUsable\":true,\"badgeNumber\":303,\"fridgeKeyNumber\":404,\"isReserved\":false,\"idFloor\":{\"id\":1}}")
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.roomNumber").value(101))
                .andExpect(jsonPath("$.roomKeyNumber").value(202))
                .andExpect(jsonPath("$.isUsable").value(true))
                .andExpect(jsonPath("$.badgeNumber").value(303))
                .andExpect(jsonPath("$.fridgeKeyNumber").value(404))
                .andExpect(jsonPath("$.isReserved").value(false))
                .andExpect(jsonPath("$.idFloor.id").value(1));

    }


    // Test de création de chambre avec une exception de duplication
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testCreateRoom_DuplicateException() throws Exception {
        // Configurer le mock pour lancer une DuplicateException lors de l'appel de la méthode create
        doThrow(new DuplicateException("Room already exists")).when(roomService).create(any(RoomDTO.class));

        // Effectuer une requête POST et vérifier que le statut de la réponse est Conflict
        mockMvc.perform(post("/api/rooms/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"roomNumber\":101,\"roomKeyNumber\":202,\"isUsable\":true,\"badgeNumber\":303,\"fridgeKeyNumber\":404,\"isReserved\":false,\"idFloor\":{\"id\":1}}")
                        .with(csrf()))
                .andExpect(status().isConflict());
    }


    // Test de création de chambre avec une exception de création
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testCreateRoom_CreationException() throws Exception {
        // Configurer le mock pour lancer une CreationException lors de l'appel de la méthode create
        doThrow(new CreationException("Error occurred while creating room")).when(roomService).create(any(RoomDTO.class));

        // Effectuer une requête POST et vérifier que le statut de la réponse est Unprocessable Entity
        mockMvc.perform(post("/api/rooms/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"roomNumber\":101,\"roomKeyNumber\":202,\"isUsable\":true,\"badgeNumber\":303,\"fridgeKeyNumber\":404,\"isReserved\":false,\"idDeposite\":1,\"idFloor\":{\"id\":1}}")
                        .with(csrf()))
                .andExpect(status().isUnprocessableEntity());
    }


    // Test de récupération de toutes les chambres
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAllRooms() throws Exception {

        // Préparation des DTO pour le test
        AfpaCenterDTO afpaCenter = new AfpaCenterDTO();
        afpaCenter.setId(1);
        afpaCenter.setCenterName("Center 1");

        FloorDTO floor = new FloorDTO();
        floor.setId(1);
        floor.setNumberFloor(1);
        floor.setIsForWomen(true);

        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(1);
        roomDTO.setRoomNumber("101");
        roomDTO.setRoomKeyNumber(202);
        roomDTO.setIsUsable(true);
        roomDTO.setBadgeNumber(303);
        roomDTO.setFridgeKeyNumber(404);
        roomDTO.setIsReserved(false);
        roomDTO.setIdFloor(floor);


        // Configuration du mock pour retourner une liste contenant le RoomDTO
        when(roomService.getAll()).thenReturn(Collections.singletonList(roomDTO));

        // Effectuer une requête GET et vérifier la réponse
        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].roomNumber").value(101))
                .andExpect(jsonPath("$[0].roomKeyNumber").value(202))
                .andExpect(jsonPath("$[0].isUsable").value(true))
                .andExpect(jsonPath("$[0].badgeNumber").value(303))
                .andExpect(jsonPath("$[0].fridgeKeyNumber").value(404))
                .andExpect(jsonPath("$[0].isReserved").value(false))
                .andExpect(jsonPath("$[0].idFloor.id").value(1))  // Vérification de l'ID du floor
                .andExpect(jsonPath("$[0].idFloor.numberFloor").value(1))  // Vérification du numéro d'étage
                .andExpect(jsonPath("$[0].idFloor.isForWomen").value(true));  // Vérification de isForWomen

    }


    // Test getAll rooms avec NoContentException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAllRooms_NoContentException() throws Exception {

        // Configurer le mock pour lancer une NoContentException
        doThrow(new NoContentException("No rooms found")).when(roomService).getAll();

        // Effectuer une requête GET et vérifier le statut de la réponse
        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isNoContent()); // 204 No Content
    }


    // Test de récupération d'une chambre par ID
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetRoomById() throws Exception {
        // Préparation d'un RoomDTO pour le test
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(1);
        roomDTO.setRoomNumber("101");
        roomDTO.setRoomKeyNumber(202);
        roomDTO.setIsUsable(true);
        roomDTO.setBadgeNumber(303);
        roomDTO.setFridgeKeyNumber(404);
        roomDTO.setIsReserved(false);

        // Préparation d'un FloorDTO pour le test
        FloorDTO floor = new FloorDTO();
        floor.setId(1);
        floor.setNumberFloor(1);
        floor.setIsForWomen(true);
        roomDTO.setIdFloor(floor); // Associer le floor à la chambre

        // Préparation d'un AfpaCenterDTO pour le test
        AfpaCenterDTO afpaCenter = new AfpaCenterDTO();
        afpaCenter.setId(1);
        afpaCenter.setCenterName("Center 1");


        // Configuration du mock pour retourner le RoomDTO lors de l'appel de la méthode getById avec l'ID 1
        when(roomService.getById(1)).thenReturn(Optional.of(roomDTO));

        // Effectuer une requête GET avec l'ID 1 et vérifier la réponse
        mockMvc.perform(get("/api/rooms/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.roomNumber").value(101))
                .andExpect(jsonPath("$.roomKeyNumber").value(202))
                .andExpect(jsonPath("$.isUsable").value(true))
                .andExpect(jsonPath("$.badgeNumber").value(303))
                .andExpect(jsonPath("$.fridgeKeyNumber").value(404))
                .andExpect(jsonPath("$.isReserved").value(false))
                .andExpect(jsonPath("$.idFloor.id").value(1))  // Vérification de l'ID du floor
                .andExpect(jsonPath("$.idFloor.numberFloor").value(1))  // Vérification du numéro d'étage
                .andExpect(jsonPath("$.idFloor.isForWomen").value(true));  // Vérification de isForWomen

    }


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetRoomById_NotFoundException() throws Exception {

        // Configuration du mock pour lancer une ResourceNotFoundException
        when(roomService.getById(1)).thenThrow(new ResourceNotFoundException("Room not found with id 1"));

        // Effectuer une requête GET avec l'ID 1 et vérifier que le statut de la réponse est Not Found
        mockMvc.perform(get("/api/rooms/{id}", 1))
                .andExpect(status().isNotFound());
    }


    // Test de mise à jour de chambre
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdateRoom() throws Exception {

        // Préparation d'un RoomDTO pour le test
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(1);
        roomDTO.setRoomNumber("101");
        roomDTO.setRoomKeyNumber(202);
        roomDTO.setIsUsable(true);
        roomDTO.setBadgeNumber(303);
        roomDTO.setFridgeKeyNumber(404);
        roomDTO.setIsReserved(false);

        // Préparation d'un FloorDTO pour le test
        FloorDTO floor = new FloorDTO();
        floor.setId(1);
        floor.setNumberFloor(1);
        floor.setIsForWomen(true);
        roomDTO.setIdFloor(floor); // Associer le floor à la chambre

        // Préparation d'un AfpaCenterDTO pour le test
        AfpaCenterDTO afpaCenter = new AfpaCenterDTO();
        afpaCenter.setId(1);
        afpaCenter.setCenterName("Center 1");


        // Configuration du mock pour retourner le RoomDTO lors de l'appel de la méthode update
        when(roomService.update(any(Integer.class), any(RoomDTO.class))).thenReturn(Optional.of(roomDTO));

        // Effectuer une requête PUT avec l'ID 1 et vérifier la réponse
        mockMvc.perform(put("/api/rooms/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"roomNumber\":101,\"roomKeyNumber\":202,\"isUsable\":true,\"badgeNumber\":303,\"fridgeKeyNumber\":404,\"isReserved\":false,\"idFloor\":{\"id\":1,\"numberFloor\":1,\"isForWomen\":true}}")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.roomNumber").value(101))
                .andExpect(jsonPath("$.roomKeyNumber").value(202))
                .andExpect(jsonPath("$.isUsable").value(true))
                .andExpect(jsonPath("$.badgeNumber").value(303))
                .andExpect(jsonPath("$.fridgeKeyNumber").value(404))
                .andExpect(jsonPath("$.isReserved").value(false))
                .andExpect(jsonPath("$.idFloor.id").value(1))  // Vérification de l'ID du floor
                .andExpect(jsonPath("$.idFloor.numberFloor").value(1))  // Vérification du numéro d'étage
                .andExpect(jsonPath("$.idFloor.isForWomen").value(true));  // Vérification de isForWomen

    }


    // Test de mise à jour de chambre avec une chambre non trouvée (similaire à testUpdateRoom_RoomNotFoundException)
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdateRoomById_NotFoundException() throws Exception {

        // Configuration du mock pour lancer une ResourceNotFoundException
        when(roomService.update(any(Integer.class), any(RoomDTO.class))).thenThrow(new ResourceNotFoundException("Room not found with id: 1"));

        // Effectuer une requête PUT avec l'ID 1 et vérifier que le statut de la réponse est Not Found
        mockMvc.perform(put("/api/rooms/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"roomNumber\":101,\"roomKeyNumber\":202,\"isUsable\":true,\"badgeNumber\":303,\"fridgeKeyNumber\":404,\"isReserved\":false,\"idFloor\":{\"id\":1}}")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }


    // Test de suppression de chambre
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDeleteRoom() throws Exception {
        // Effectuer une requête DELETE avec l'ID 1 et vérifier que le statut de la réponse est OK
        mockMvc.perform(delete("/api/rooms/delete/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }


    // Test de suppression de chambre avec une exception de suppression
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDeleteRoom_DeleteException() throws Exception {
        // Configurer le mock pour lancer une DeleteException lors de l'appel de la méthode delete
        doThrow(new ResourceNotFoundException("Room deletion failed")).when(roomService).deleteById(1);

        // Effectuer une requête DELETE avec l'ID 1 et vérifier que le statut de la réponse est Conflict
        mockMvc.perform(delete("/api/rooms/delete/1")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }


    // Test de récupération de toutes les chambres par centre Afpa avec contenu
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAllByAfpaCenter() throws Exception {

        // Préparation des DTO pour le test
        AfpaCenterDTO afpaCenter = new AfpaCenterDTO();
        afpaCenter.setId(1);
        afpaCenter.setCenterName("Center 1");

        FloorDTO floor = new FloorDTO();
        floor.setId(1);
        floor.setNumberFloor(1);
        floor.setIsForWomen(true);

        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(1);
        roomDTO.setRoomNumber("101");
        roomDTO.setRoomKeyNumber(202);
        roomDTO.setIsUsable(true);
        roomDTO.setBadgeNumber(303);
        roomDTO.setFridgeKeyNumber(404);
        roomDTO.setIsReserved(false);
        roomDTO.setIdFloor(floor);


        // Configuration du mock pour retourner une liste contenant le RoomDTO
        when(roomService.getAllByAfpaCenter(1)).thenReturn(Collections.singletonList(roomDTO));

        // Effectuer une requête GET et vérifier la réponse
        mockMvc.perform(get("/api/rooms/afpa-center/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].roomNumber").value(101))
                .andExpect(jsonPath("$[0].roomKeyNumber").value(202))
                .andExpect(jsonPath("$[0].isUsable").value(true))
                .andExpect(jsonPath("$[0].badgeNumber").value(303))
                .andExpect(jsonPath("$[0].fridgeKeyNumber").value(404))
                .andExpect(jsonPath("$[0].isReserved").value(false))
                //andExpect(jsonPath("$[0].idDeposit").value(1)) // Vérification de l'ID du dépôt
                .andExpect(jsonPath("$[0].idFloor.id").value(1)); // Vérification de l'ID de l'étage

    }


    // Test de récupération de toutes les chambres par centre Afpa avec NoContentException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAllByAfpaCenter_NoContentException() throws Exception {
        // Configurer le mock pour lancer une NoContentException
        doThrow(new NoContentException("No rooms found")).when(roomService).getAllByAfpaCenter(1);

        // Effectuer une requête GET et vérifier le statut de la réponse
        mockMvc.perform(get("/api/rooms/afpa-center/1"))
                .andExpect(status().isNoContent()); // 204 No Content
    }


    // Test de récupération des chambres réservées par centre Afpa avec contenu
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetRoomByReservationAndAfpaCenter() throws Exception {

        // Préparation des DTO pour le test
        AfpaCenterDTO afpaCenter = new AfpaCenterDTO();
        afpaCenter.setId(1);
        afpaCenter.setCenterName("Center 1");

        FloorDTO floor = new FloorDTO();
        floor.setId(1);
        floor.setNumberFloor(1);
        floor.setIsForWomen(true);

        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(1);
        roomDTO.setRoomNumber("101");
        roomDTO.setRoomKeyNumber(202);
        roomDTO.setIsUsable(true);
        roomDTO.setBadgeNumber(303);
        roomDTO.setFridgeKeyNumber(404);
        roomDTO.setIsReserved(true); // Chambre réservée
        roomDTO.setIdFloor(floor);


        // Configuration du mock pour retourner une liste contenant le RoomDTO
        when(roomService.getByReservedRoomAndIdAfpaCenter(1)).thenReturn(Collections.singletonList(roomDTO));

        // Effectuer une requête GET et vérifier la réponse
        mockMvc.perform(get("/api/rooms/reserved/afpa-center/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].roomNumber").value(101))
                .andExpect(jsonPath("$[0].roomKeyNumber").value(202))
                .andExpect(jsonPath("$[0].isUsable").value(true))
                .andExpect(jsonPath("$[0].badgeNumber").value(303))
                .andExpect(jsonPath("$[0].fridgeKeyNumber").value(404))
                .andExpect(jsonPath("$[0].isReserved").value(true))
                 //.andExpect(jsonPath("$[0].idDeposit").value(1)) // Vérification de l'ID du dépôt
                .andExpect(jsonPath("$[0].idFloor.id").value(1)); // Vérification de l'ID de l'étage

    }


    // Test de récupération des chambres réservées par centre Afpa avec NoContentException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetRoomByReservationAndAfpaCenter_NoContentException() throws Exception {
        // Configurer le mock pour lancer une NoContentException
        doThrow(new NoContentException("No reserved rooms found")).when(roomService).getByReservedRoomAndIdAfpaCenter(1);

        // Effectuer une requête GET et vérifier le statut de la réponse
        mockMvc.perform(get("/api/rooms/reserved/afpa-center/1"))
                .andExpect(status().isNoContent()); // 204 No Content
    }


    // Test de récupération des chambres libres par centre Afpa avec contenu
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetRoomByAvailabilityAndAfpaCenter() throws Exception {
        // Préparation des DTO pour le test
        AfpaCenterDTO afpaCenter = new AfpaCenterDTO();
        afpaCenter.setId(1);
        afpaCenter.setCenterName("Center 1");

        FloorDTO floor = new FloorDTO();
        floor.setId(1);
        floor.setNumberFloor(1);
        floor.setIsForWomen(true);

        // Création d'un RoomDTO pour une chambre libre
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(1);
        roomDTO.setRoomNumber("101");
        roomDTO.setRoomKeyNumber(202);
        roomDTO.setIsUsable(true);
        roomDTO.setBadgeNumber(303);
        roomDTO.setFridgeKeyNumber(404);
        roomDTO.setIsReserved(false); // Chambre libre
        roomDTO.setIdFloor(floor); // Liaison avec l'étage


        // Configuration du mock pour retourner une liste contenant le RoomDTO
        when(roomService.getByAvailableRoomAndIdAfpaCenter(1)).thenReturn(Collections.singletonList(roomDTO));

        // Effectuer une requête GET et vérifier la réponse
        mockMvc.perform(get("/api/rooms/available/afpa-center/{id}", 1))
                .andExpect(status().isOk()) // Vérifier que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$[0].id").value(1)) // Vérifier que l'ID de la chambre est 1
                .andExpect(jsonPath("$[0].roomNumber").value(101)) // Vérifier le numéro de la chambre
                .andExpect(jsonPath("$[0].roomKeyNumber").value(202)) // Vérifier le numéro de clé de la chambre
                .andExpect(jsonPath("$[0].isUsable").value(true)) // Vérifier si la chambre est utilisable
                .andExpect(jsonPath("$[0].badgeNumber").value(303)) // Vérifier le numéro de badge
                .andExpect(jsonPath("$[0].fridgeKeyNumber").value(404)) // Vérifier le numéro de clé du réfrigérateur
                .andExpect(jsonPath("$[0].isReserved").value(false)) // Vérifier si la chambre est réservée (doit être false)
                 //.andExpect(jsonPath("$[0].idDeposit").value(1)) // Vérifier l'ID du dépôt associé à la chambre
                .andExpect(jsonPath("$[0].idFloor.id").value(1)); // Vérifier l'ID de l'étage de la chambre

    }


    // Test de récupération des chambres libres par centre Afpa avec NoContentException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetRoomByAvailabilityAndAfpaCenter_NoContentException() throws Exception {
        // Configurer le mock pour lancer une NoContentException
        doThrow(new NoContentException("No available rooms found")).when(roomService).getByAvailableRoomAndIdAfpaCenter(1);

        // Effectuer une requête GET et vérifier le statut de la réponse
        mockMvc.perform(get("/api/rooms/available/afpa-center/1"))
                .andExpect(status().isNoContent()); // 204 No Content
    }


    // Test de récupération des chambres occupées par centre Afpa avec contenu
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetByOccupiedRoomAndAfpaCenter() throws Exception {
        // Préparation des DTO pour le test
        AfpaCenterDTO afpaCenter = new AfpaCenterDTO();
        afpaCenter.setId(1);
        afpaCenter.setCenterName("Center 1");

        FloorDTO floor = new FloorDTO();
        floor.setId(1);
        floor.setNumberFloor(1);
        floor.setIsForWomen(true);

        // Création d'un RoomDTO pour une chambre occupée
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(1);
        roomDTO.setRoomNumber("101");
        roomDTO.setRoomKeyNumber(202);
        roomDTO.setIsUsable(true);
        roomDTO.setBadgeNumber(303);
        roomDTO.setFridgeKeyNumber(404);
        roomDTO.setIsReserved(true); // Chambre occupée
        //roomDTO.setIdDeposit(1); // ID du dépôt associé à la chambre
        roomDTO.setIdFloor(floor); // Liaison avec l'étage

        // Configuration du mock pour retourner une liste contenant le RoomDTO
        when(roomService.getByOccupiedRoomAndIdAfpaCenter(1)).thenReturn(Collections.singletonList(roomDTO));

        // Effectuer une requête GET et vérifier la réponse
        mockMvc.perform(get("/api/rooms/occupied/afpa-center/{id}", 1))
                .andExpect(status().isOk()) // Vérifier que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$[0].id").value(1)) // Vérifier que l'ID de la chambre est 1
                .andExpect(jsonPath("$[0].roomNumber").value(101)) // Vérifier le numéro de la chambre
                .andExpect(jsonPath("$[0].roomKeyNumber").value(202)) // Vérifier le numéro de clé de la chambre
                .andExpect(jsonPath("$[0].isUsable").value(true)) // Vérifier si la chambre est utilisable
                .andExpect(jsonPath("$[0].badgeNumber").value(303)) // Vérifier le numéro de badge
                .andExpect(jsonPath("$[0].fridgeKeyNumber").value(404)) // Vérifier le numéro de clé du réfrigérateur
                .andExpect(jsonPath("$[0].isReserved").value(true)) // Vérifier si la chambre est réservée (doit être true pour occupée)
                //.andExpect(jsonPath("$[0].idDeposit").value(1)) // Vérifier l'ID du dépôt associé à la chambre
                .andExpect(jsonPath("$[0].idFloor.id").value(1)); // Vérifier l'ID de l'étage de la chambre

    }


    // Test de récupération des chambres occupées par centre Afpa avec NoContentException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetByOccupiedRoomAndAfpaCenter_NoContentException() throws Exception {
        // Configurer le mock pour lancer une NoContentException
        doThrow(new NoContentException("No occupied rooms found")).when(roomService).getByOccupiedRoomAndIdAfpaCenter(1);

        // Effectuer une requête GET et vérifier le statut de la réponse
        mockMvc.perform(get("/api/rooms/occupied/afpa-center/1"))
                .andExpect(status().isNoContent()); // 204 No Content
    }


    // Test de récupération des chambres pour femmes par centre Afpa avec contenu
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetByWomanRoomAndAfpaCenter() throws Exception {
        // Préparation des DTO pour le test
        AfpaCenterDTO afpaCenter = new AfpaCenterDTO();
        afpaCenter.setId(1);
        afpaCenter.setCenterName("Center 1");

        FloorDTO floor = new FloorDTO();
        floor.setId(1);
        floor.setNumberFloor(1);
        floor.setIsForWomen(true); // Marquer l'étage comme étant pour les femmes

        // Création d'un RoomDTO pour une chambre réservée pour femmes
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(1);
        roomDTO.setRoomNumber("101");
        roomDTO.setRoomKeyNumber(202);
        roomDTO.setIsUsable(true);
        roomDTO.setBadgeNumber(303);
        roomDTO.setFridgeKeyNumber(404);
        roomDTO.setIsReserved(true); // Chambre réservée
        //roomDTO.setIdDeposit(1); // ID du dépôt associé à la chambre
        roomDTO.setIdFloor(floor); // Liaison avec l'étage


        // Configuration du mock pour retourner une liste contenant le RoomDTO
        when(roomService.getByWomanRoomAndIdAfpaCenter(1)).thenReturn(Collections.singletonList(roomDTO));

        // Effectuer une requête GET et vérifier la réponse
        mockMvc.perform(get("/api/rooms/women/afpa-center/{id}", 1))
                .andExpect(status().isOk()) // Vérifier que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$[0].id").value(1)) // Vérifier que l'ID de la chambre est 1
                .andExpect(jsonPath("$[0].roomNumber").value(101)) // Vérifier le numéro de la chambre
                .andExpect(jsonPath("$[0].roomKeyNumber").value(202)) // Vérifier le numéro de clé de la chambre
                .andExpect(jsonPath("$[0].isUsable").value(true)) // Vérifier si la chambre est utilisable
                .andExpect(jsonPath("$[0].badgeNumber").value(303)) // Vérifier le numéro de badge
                .andExpect(jsonPath("$[0].fridgeKeyNumber").value(404)) // Vérifier le numéro de clé du réfrigérateur
                .andExpect(jsonPath("$[0].isReserved").value(true)) // Vérifier si la chambre est réservée (doit être true pour réservée)
                //.andExpect(jsonPath("$[0].idDeposit").value(1)) // Vérifier l'ID du dépôt associé à la chambre
                .andExpect(jsonPath("$[0].idFloor.id").value(1)); // Vérifier l'ID de l'étage de la chambre

    }


    // Test de récupération des chambres pour femmes par centre Afpa avec NoContentException
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetByWomanRoomAndAfpaCenter_NoContentException() throws Exception {
        // Configurer le mock pour lancer une NoContentException
        doThrow(new NoContentException("No rooms for women found")).when(roomService).getByWomanRoomAndIdAfpaCenter(1);

        // Effectuer une requête GET et vérifier le statut de la réponse
        mockMvc.perform(get("/api/rooms/women/afpa-center/1"))
                .andExpect(status().isNoContent()); // 204 No Content
    }

}
