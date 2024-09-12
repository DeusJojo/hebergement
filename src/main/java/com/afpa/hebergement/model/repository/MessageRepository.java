package com.afpa.hebergement.model.repository;


import com.afpa.hebergement.model.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MessageRepository extends JpaRepository<Message, Integer> {

    //rechercher les messages par id intendant
    List<Message> findByIdIntendant_Id(Integer idIntendant);

    //rechercher les messages par id user
    List<Message> findByIdUser_Id(Integer idUser);

    //rechercher tous les messages par centre afpa
    List<Message> findByIdUser_IdAfpaCenter_Id(Integer idAfpaCenter);

    //rechercher tous les messages par centre afpa avec pagination
    Page<Message> findByIdUser_IdAfpaCenter_Id(Integer afpaCenter, Pageable pageable);
}
