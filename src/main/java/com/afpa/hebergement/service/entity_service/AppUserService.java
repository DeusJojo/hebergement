package com.afpa.hebergement.service.entity_service;

import com.afpa.hebergement.model.dto.AppUserDTO;
import com.afpa.hebergement.model.dto.PasswordDTO;
import com.afpa.hebergement.model.dto.UserDTO;
import com.afpa.hebergement.service.GenericService;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;


public interface AppUserService extends GenericService<AppUserDTO, Integer> {

    //methode pour update les informations d'un user (avec role_user)
    Optional<UserDTO> updateUser(Integer id, UserDTO userDto);

    //methode pour mettre à jour le mdp
    void updatePassword(Integer id, PasswordDTO passwordDto);

    //récupérer tous les users d'un centre afpa
    List<AppUserDTO> getAllByAfpaCenter(Integer idAfpaCenter);

    //récupérer les users par centre afpa avec pagination
    Page<AppUserDTO> getAllByAfpaCenter(Integer idAfpaCenter, Integer page, Integer size);

    //récupérer un user par son numéro de bénéficiaire
    Optional<AppUserDTO> getByBeneficiaryNumber(String beneficiaryNumber);

    //récupérer un user par son email
    Optional<AppUserDTO> getByEmail(String email);



}
