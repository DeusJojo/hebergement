package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.AppUserDTO;
import com.afpa.hebergement.model.entity.AppUser;

public class AppUserMapper {

    // Constructeur privé pour empêcher l'instanciation de cette classe
    private AppUserMapper() {
    }


    // Méthode pour mapper une entité AppUser vers un DTO AppUserDTO
    public static AppUserDTO mapToAppUserDTO(AppUser appUser) {

        // Créer un objet AppUserDTO
        AppUserDTO appUserDTO = new AppUserDTO();

        // Affecte les champs de l'entité AppUser au DTO AppUserDTO
        appUserDTO.setId(appUser.getId());
        appUserDTO.setBeneficiaryNumber(appUser.getBeneficiaryNumber());
        appUserDTO.setName(appUser.getName().toUpperCase());
        appUserDTO.setFirstname(appUser.getFirstname());
        appUserDTO.setEmail(appUser.getEmail());
        appUserDTO.setPassword(appUser.getPassword());
        appUserDTO.setBirthdate(appUser.getBirthdate());
        appUserDTO.setBirthplace(appUser.getBirthplace());
        appUserDTO.setPhoneNumber(appUser.getPhoneNumber());
        appUserDTO.setIsBlacklisted(appUser.getIsBlacklisted());
        appUserDTO.setRegistrationDate(appUser.getRegistrationDate());
        appUserDTO.setAddressUser(appUser.getAddressUser());
        appUserDTO.setComplementUserAddress(appUser.getComplementUserAddress()); //attribut pouvant être null

        // Mappage des Many-to-One
        appUserDTO.setIdRole(RoleMapper.mapToRoleDTO(appUser.getIdRole()));
        appUserDTO.setIdCivility(CivilityMapper.mapToCivilityDTO(appUser.getIdCivility()));
        appUserDTO.setIdCity(CityMapper.mapToCityDto(appUser.getIdCity()));
        appUserDTO.setIdAfpaCenter(AfpaCenterMapper.mapToAfpaCenterDto(appUser.getIdAfpaCenter()));
        appUserDTO.setIdContactPerson(ContactPersonMapper.mapToContactPersonDto(appUser.getIdContactPerson()));

        // Retourne le DTO AppUserDTO
        return appUserDTO;
    }


    // Méthode pour mapper un DTO AppUserDTO vers une entité AppUser
    public static AppUser mapToAppUser(AppUserDTO appUserDto) {

        // Créer un objet AppUser
        AppUser appUser = new AppUser();

        // Affecte les champs du DTO AppUserDTO à l'entité AppUser
        appUser.setId(appUserDto.getId());
        appUser.setBeneficiaryNumber(appUserDto.getBeneficiaryNumber().trim());
        appUser.setName(appUserDto.getName().toLowerCase().trim());
        appUser.setFirstname(appUserDto.getFirstname().toLowerCase().trim());
        appUser.setEmail(appUserDto.getEmail().toLowerCase().trim());
        appUser.setPassword(appUserDto.getPassword());
        appUser.setBirthdate(appUserDto.getBirthdate());
        appUser.setBirthplace(appUserDto.getBirthplace());
        appUser.setPhoneNumber(appUserDto.getPhoneNumber());
        appUser.setIsBlacklisted(appUserDto.getIsBlacklisted());
        appUser.setRegistrationDate(appUserDto.getRegistrationDate());
        appUser.setAddressUser(appUserDto.getAddressUser().trim());
        appUser.setComplementUserAddress(appUserDto.getComplementUserAddress().trim()); //attribut pouvant être null

        // Mappage des Many-to-One
        appUser.setIdRole(RoleMapper.mapToRoleEntity(appUserDto.getIdRole()));
        appUser.setIdCivility(CivilityMapper.mapToCivilityEntity(appUserDto.getIdCivility()));
        appUser.setIdCity(CityMapper.mapToCity(appUserDto.getIdCity()));
        appUser.setIdAfpaCenter(AfpaCenterMapper.mapToAfpaCenter(appUserDto.getIdAfpaCenter()));
        appUser.setIdContactPerson(ContactPersonMapper.mapToContactPerson(appUserDto.getIdContactPerson()));

        // Retourne l'entité AppUser
        return appUser;
    }

}
