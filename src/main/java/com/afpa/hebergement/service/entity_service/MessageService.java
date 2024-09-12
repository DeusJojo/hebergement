package com.afpa.hebergement.service.entity_service;

import com.afpa.hebergement.model.dto.MessageDTO;
import com.afpa.hebergement.service.GenericService;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MessageService extends GenericService<MessageDTO, Integer> {

    //récupérer les messages par l'id intendant
    List<MessageDTO> getMessagesByIntendant(Integer idIntendant);

    //récupérer les messages par l'id user
    List<MessageDTO> getMessagesByUser(Integer idUser);

    //récupérer les messages par centre afpa
    List<MessageDTO> getAllMessagesByAfpaCenter(Integer idAfpaCenter);

    //récupérer les messages par centre afpa avec pagination
    Page<MessageDTO> getAllMessagesByAfpaCenter(Integer idAfpaCenter, Integer page, Integer size);

    //créer un message par id intendant et id user
    MessageDTO create (MessageDTO messageDto, Integer idIntendant, Integer idUser);

}
