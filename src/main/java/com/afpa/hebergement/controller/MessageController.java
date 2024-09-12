package com.afpa.hebergement.controller;


import com.afpa.hebergement.model.dto.MessageDTO;
import com.afpa.hebergement.service.entity_service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Message", description = "Endpoints to manage messages")
@RestController
@RequestMapping("/api/messages")
@AllArgsConstructor
public class MessageController {

    private final MessageService messageService;


    // Endpoint pour créer un nouveau message
    @PostMapping("/create/{idIntendant}/{idUser}")
    @Operation(summary = "Create new message by intendant and user")
    public ResponseEntity<MessageDTO> createMessage(@PathVariable("idIntendant") Integer idIntendant, @PathVariable("idUser") Integer idUser, @Valid @RequestBody MessageDTO messageDto) {
        // Appel du service pour créer un nouveau message à partir du DTO et des IDs
        MessageDTO createdMessage = messageService.create(messageDto, idIntendant, idUser);
        // Retourne le message créé avec un statut HTTP 201 (Created)
        return new ResponseEntity<>(createdMessage, HttpStatus.CREATED);
    }


    // Endpoint pour récupérer tous les messages par centre Afpa sans pagination
    @GetMapping("/afpa-center/{idAfpaCenter}")
    @Operation(summary = "Get all messages by Afpa Center")
    public ResponseEntity<List<MessageDTO>> getAllMessagesByAfpaCenter(@PathVariable("idAfpaCenter") Integer idAfpaCenter) {
        // Appel du service pour obtenir tous les messages par centre Afpa
        List<MessageDTO> messages = messageService.getAllMessagesByAfpaCenter(idAfpaCenter);
        // Retourne la liste des messages avec un statut HTTP 200 (OK)
        return ResponseEntity.ok(messages);
    }


    // Endpoint pour récupérer tous les messages par centre Afpa avec pagination
    @GetMapping("/afpa-center/{idAfpaCenter}/page")
    @Operation(summary = "Get messages by Afpa Center with pagination")
    public ResponseEntity<Page<MessageDTO>> getMessagesByAfpaCenterPage(
            @PathVariable("idAfpaCenter") Integer idAfpaCenter,
            // Définit un paramètre de requête "page" avec une valeur par défaut de 0 (1ère page)
            @RequestParam(defaultValue = "0") int page,
            // Définit un paramètre de requête "size" avec une valeur par défaut de 10 (10 éléments par page)
            @RequestParam(defaultValue = "10") int size) {

        // Appel du service pour obtenir les messages par centre Afpa avec pagination
        Page<MessageDTO> messagesPage = messageService.getAllMessagesByAfpaCenter(idAfpaCenter, page, size);
        // Retourne la page des messages avec un statut HTTP 200 (OK)
        return ResponseEntity.ok(messagesPage);
    }


    // Endpoint pour récupérer un message par son ID
    @GetMapping("/{id}")
    @Operation(summary = "Get message by ID")
    public ResponseEntity<MessageDTO> getMessageById(@PathVariable("id") Integer idMessage) {
        // Appel du service pour obtenir un message par son ID
        Optional<MessageDTO> message = messageService.getById(idMessage);

        // Retourne le message avec un statut HTTP 200 (OK) s'il est présent
        return message.map(ResponseEntity::ok)
                // Retourne une réponse avec un statut HTTP 404 (Not Found) si le message n'est pas trouvé
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Endpoint pour récupérer les messages par intendant
    @GetMapping("/intendant/{idIntendant}")
    @Operation(summary = "Get messages by Intendant ID")
    public ResponseEntity<List<MessageDTO>> getMessagesByIntendant(@PathVariable("idIntendant") Integer idIntendant) {
        // Appel du service pour obtenir les messages par intendant
        List<MessageDTO> messages = messageService.getMessagesByIntendant(idIntendant);
        // Retourne la liste des messages avec un statut HTTP 200 (OK)
        return ResponseEntity.ok(messages);
    }


    // Endpoint pour récupérer les messages par utilisateur
    @GetMapping("/user/{idUser}")
    @Operation(summary = "Get messages by User ID")
    public ResponseEntity<List<MessageDTO>> getMessagesByUser(@PathVariable("idUser") Integer idUser) {
        // Appel du service pour obtenir les messages par utilisateur
        List<MessageDTO> messages = messageService.getMessagesByUser(idUser);
        // Retourne la liste des messages avec un statut HTTP 200 (OK)
        return ResponseEntity.ok(messages);
    }


    // Endpoint pour supprimer un message par son ID
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete message by ID")
    public ResponseEntity<HttpStatus> deleteMessage(@PathVariable("id") Integer idMessage) {
        // Appel du service pour supprimer le message par son ID
        messageService.deleteById(idMessage);
        // Retourne un statut HTTP 204 (No Content) après la suppression
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

