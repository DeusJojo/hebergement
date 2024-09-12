package com.afpa.hebergement.model.mapper;


import com.afpa.hebergement.model.dto.MessageDTO;
import com.afpa.hebergement.model.entity.Message;

public class MessageMapper {


    // Constructeur privé pour empêcher l'instanciation de cette classe
    private MessageMapper() {

    }


    //Méthode pour mapper une entité Message vers un DTO MessageDTO.
    public static MessageDTO mapToMessageDTO(Message message) {

        // Crée un objet MessageDTO
        MessageDTO messageDTO = new MessageDTO();

        // Affecte les champs de l'entité Message au DTO MessageDTO
        messageDTO.setId(message.getId());
        messageDTO.setContent(message.getContent());
        messageDTO.setMessageDate(message.getMessageDate());

        // Mappage de Many to One
        messageDTO.setIdIntendant(IntendantMapper.mapToIntendantDTO(message.getIdIntendant()));
        messageDTO.setIdUser(AppUserMapper.mapToAppUserDTO(message.getIdUser()));

        // Retourne le DTO MessageDTO
        return messageDTO;
    }


    //Méthode pour mapper un DTO MessageDTO vers une entité Message.
    public static Message mapToMessageEntity(MessageDTO messageDto) {

        // Crée un objet Message
        Message message = new Message();

        // Affecte les champs du DTO MessageDTO à l'entité Message
        message.setId(messageDto.getId());
        message.setContent(messageDto.getContent().trim());
        message.setMessageDate(messageDto.getMessageDate());

        // Mappage de Many to One
        message.setIdIntendant(IntendantMapper.mapToIntendantEntity(messageDto.getIdIntendant()));
        message.setIdUser(AppUserMapper.mapToAppUser(messageDto.getIdUser()));

        // Retourne l'entité Message
        return message;
    }
}
