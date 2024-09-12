package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.UserDTO;
import com.afpa.hebergement.model.entity.AppUser;

public class UserMapper {

    // Constructeur privé pour empêcher l'instanciation de cette classe
    private UserMapper() {
    }


    // Méthode pour mapper une entité AppUser vers un DTO UserDTO
    public static UserDTO mapToUserDTO(AppUser appUser) {

        // Créer un objet UserDTO
        UserDTO userDTO = new UserDTO();

        // Affecte les champs de l'entité AppUser au DTO UserDTO
        userDTO.setEmail(appUser.getEmail());
        userDTO.setPhoneNumber(appUser.getPhoneNumber());
        userDTO.setAddressUser(appUser.getAddressUser());

        userDTO.setComplementUserAddress(appUser.getComplementUserAddress().trim()); //attribut pouvant être null

        // Mappage des Many-to-One
        userDTO.setIdCivility(CivilityMapper.mapToCivilityDTO(appUser.getIdCivility()));
        userDTO.setIdCity(CityMapper.mapToCityDto(appUser.getIdCity()));
        userDTO.setIdContactPerson(ContactPersonMapper.mapToContactPersonDto(appUser.getIdContactPerson()));

        // Retourne le DTO UserDTO
        return userDTO;
    }


    // Méthode pour mapper un DTO UserDTO vers une entité AppUser
    public static AppUser mapToAppUser(UserDTO userDto) {

        // Créer un objet AppUser
        AppUser appUser = new AppUser();

        // Affecte les champs du DTO UserDTO à l'entité AppUser
        appUser.setEmail(userDto.getEmail().toLowerCase().trim());
        appUser.setPhoneNumber(userDto.getPhoneNumber());
        appUser.setAddressUser(userDto.getAddressUser().trim());

        appUser.setComplementUserAddress(userDto.getComplementUserAddress().trim()); //attribut pouvant être null

        // Mappage des Many-to-One
        appUser.setIdCivility(CivilityMapper.mapToCivilityEntity(userDto.getIdCivility()));
        appUser.setIdCity(CityMapper.mapToCity(userDto.getIdCity()));
        appUser.setIdContactPerson(ContactPersonMapper.mapToContactPerson(userDto.getIdContactPerson()));

        // Retourne l'entité AppUser
        return appUser;
    }

}
