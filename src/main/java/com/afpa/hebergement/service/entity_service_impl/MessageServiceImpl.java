package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.*;
import com.afpa.hebergement.model.dto.MessageDTO;
import com.afpa.hebergement.model.entity.*;
import com.afpa.hebergement.model.mapper.MessageMapper;
import com.afpa.hebergement.model.repository.AfpaCenterRepository;
import com.afpa.hebergement.model.repository.MessageRepository;
import com.afpa.hebergement.model.repository.IntendantRepository;
import com.afpa.hebergement.model.repository.AppUserRepository;
import com.afpa.hebergement.service.entity_service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {


    public static final String NO_MESSAGES_FOUND = "No messages found";
    private final MessageRepository messageRepository;
    private final IntendantRepository intendantRepository;
    private final AppUserRepository appUserRepository;
    private final AfpaCenterRepository afpaCenterRepository;


    //créer un message par id intendant et id user
    @Override
    public MessageDTO create(MessageDTO messageDto, Integer intendantId, Integer userId) {

        // Mettre le message à la date du jour
        messageDto.setMessageDate(LocalDate.now());

        // Recherche de l'ID de l'intendant dans le repository
        Intendant intendant = intendantRepository.findById(intendantId)
                .orElseThrow(() -> new ResourceNotFoundException("Intendant ID not found"));

        // Recherche de l'ID de l'utilisateur dans le repository
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User ID not found"));


        // Vérifiez si l'ID de l'intendant et l'ID de l'utilisateur sont les mêmes
        if (intendant.getIdUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Intendant ID and User ID cannot be the same");
        }

        // Mappe le DTO en entité Message
        Message message = MessageMapper.mapToMessageEntity(messageDto);

        // Assigner les entités trouvées au message
        message.setIdIntendant(intendant);
        message.setIdUser(user);

        try {

            // Sauvegarde l'entité Message dans le repository
            Message savedMessage = messageRepository.save(message);

            // Mappe l'entité Message sauvegardée en DTO et la retourne
            return MessageMapper.mapToMessageDTO(savedMessage);

        } catch (DataIntegrityViolationException e) {
            // Capturer une exception de violation d'intégrité des données
            throw new CreationException("Error occurred while creating message");
        }
    }


    @Override
    public List<MessageDTO> getAllMessagesByAfpaCenter(Integer idAfpaCenter) {

        //recherche de l'id du centre afpa dans le repository
        AfpaCenter afpaCenter = afpaCenterRepository.findById(idAfpaCenter)
                .orElseThrow(() -> new ResourceNotFoundException("Afpa Center ID not found: " + idAfpaCenter));

        // Cherche les entités messages par id afpa center
        List<Message> messagesList = messageRepository.findByIdUser_IdAfpaCenter_Id(afpaCenter.getId());

        // Vérifie si la liste est vide
        if (messagesList.isEmpty()) {
            // Lance une ResourceNotFoundException si aucune entité n'est trouvée
            throw new NoContentException(NO_MESSAGES_FOUND);
        }

        try {
            // Mappe les entités Room en DTO et retourne la liste
            return messagesList.stream()
                    .map(MessageMapper::mapToMessageDTO)
                    .toList();

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred to recover appointment with ID: " + idAfpaCenter);
        }

    }


    @Override
    public Page<MessageDTO> getAllMessagesByAfpaCenter(Integer idAfpaCenter, Integer page, Integer size) {

        // Recherche de l'id du centre Afpa dans le repository
        AfpaCenter afpaCenter = afpaCenterRepository.findById(idAfpaCenter)
                .orElseThrow(() -> new ResourceNotFoundException("Afpa Center ID not found: " + idAfpaCenter));

        // Crée un objet Pageable avec le numéro de page, la taille de la page, et le tri par ID croissant
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        // Cherche les entités messages par id Afpa center avec pagination
        Page<Message> messagesPage = messageRepository.findByIdUser_IdAfpaCenter_Id(afpaCenter.getId(), pageable);

        // Vérifie si la liste est vide
        if (messagesPage.isEmpty()) {
            // Lance une NoContentException si aucune entité n'est trouvée
            throw new NoContentException(NO_MESSAGES_FOUND);
        }

        try {
            // Mappe les entités Message en DTO et retourne la page
            return messagesPage.map(MessageMapper::mapToMessageDTO);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred to recover messages for Afpa Center ID: " + idAfpaCenter);
        }
    }


    @Override
    public Optional<MessageDTO> getById(Integer id) {

        // Cherche une entité Message par son identifiant
        Message findMessage = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with ID: " + id));

        try {
            // Convertit l'entité Message en MessageDTO par le MessageMapper
            MessageDTO messageDTO = MessageMapper.mapToMessageDTO(findMessage);

            // Retourne le MessageDTO encapsulé dans un Optional
            return Optional.of(messageDTO);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred to recover message with ID: " + id);
        }
    }


    @Override
    public List<MessageDTO> getMessagesByIntendant(Integer idIntendant) {

        // Recherche de l'intendant dans le repository
        Intendant intendant = intendantRepository.findById(idIntendant)
                .orElseThrow(() -> new ResourceNotFoundException("Intendant ID not found: " + idIntendant));

        // Cherche les entités messages par id intendant
        List<Message> messagesList = messageRepository.findByIdIntendant_Id(intendant.getId());

        // Vérifie si la liste est vide
        if (messagesList.isEmpty()) {
            // Lance une ResourceNotFoundException si aucune entité n'est trouvée
            throw new NoContentException(NO_MESSAGES_FOUND);
        }

        try {
            // Mappe les entités Message en DTO et retourne la liste
            return messagesList.stream()
                    .map(MessageMapper::mapToMessageDTO)
                    .toList();

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred to recover messages for Intendant ID: " + idIntendant);
        }
    }


    @Override
    public List<MessageDTO> getMessagesByUser(Integer idUser) {

        // Recherche de l'utilisateur dans le repository
        AppUser user = appUserRepository.findById(idUser)
                .orElseThrow(() -> new ResourceNotFoundException("User ID not found: " + idUser));

        // Cherche les entités messages par id utilisateur
        List<Message> messagesList = messageRepository.findByIdUser_Id(user.getId());

        // Vérifie si la liste est vide
        if (messagesList.isEmpty()) {
            // Lance une ResourceNotFoundException si aucune entité n'est trouvée
            throw new NoContentException(NO_MESSAGES_FOUND);
        }

        try {
            // Mappe les entités Message en DTO et retourne la liste
            return messagesList.stream()
                    .map(MessageMapper::mapToMessageDTO)
                    .toList();

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred to recover messages for User ID: " + idUser);
        }
    }


    @Override
    public void deleteById(Integer id) {
        // Recherche de l'entité Message par son identifiant
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));

        try {
            // Supprime l'entité Message trouvée
            messageRepository.delete(message);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("An unexpected error occurred while deleting the message with id: " + id);
        }
    }

}
