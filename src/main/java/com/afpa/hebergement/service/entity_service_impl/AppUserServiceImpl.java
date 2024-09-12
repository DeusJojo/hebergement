package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.*;
import com.afpa.hebergement.model.dto.AppUserDTO;
import com.afpa.hebergement.model.dto.PasswordDTO;
import com.afpa.hebergement.model.dto.UserDTO;
import com.afpa.hebergement.model.entity.*;
import com.afpa.hebergement.model.enum_role.RoleType;
import com.afpa.hebergement.model.mapper.*;
import com.afpa.hebergement.model.repository.*;
import com.afpa.hebergement.service.entity_service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class AppUserServiceImpl implements AppUserService {


    public static final String NO_USERS_FOUND = "No users found";
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final CivilityRepository civilityRepository;
    private final CityRepository cityRepository;
    private final AfpaCenterRepository afpaCenterRepository;
    private final ContactPersonRepository contactPersonRepository;
    private final IntendantRepository intendantRepository;

    private PasswordEncoder passwordEncoder;


    // Méthode pour créer un nouvel utilisateur
    @Override
    public AppUserDTO create(AppUserDTO appUserDto) {

        // Vérifier si l'utilisateur existe déjà
        Optional<AppUser> existingUser = appUserRepository.findByBeneficiaryNumber(appUserDto.getBeneficiaryNumber());

        if (existingUser.isPresent()) {
            // Lancer une exception si l'user existe déjà
            throw new DuplicateException("User already exists");
        }

        // Recherche et validation de l'id du rôle
        Role role = roleRepository.findById(appUserDto.getIdRole().getId()).orElse(null);
        if (role == null) {
            throw new ResourceNotFoundException("Role not found");
        }

        // Recherche et validation de l'id de la civilité
        Civility civility = civilityRepository.findById(appUserDto.getIdCivility().getId()).orElse(null);
        if (civility == null) {
            throw new ResourceNotFoundException("Civility not found");
        }

        // Recherche et validation de l'id de la ville
        City city = cityRepository.findById(appUserDto.getIdCity().getId()).orElse(null);
        if (city == null) {
            throw new ResourceNotFoundException("City not found");
        }

        // Recherche et validation de l'id du centre Afpa
        AfpaCenter afpaCenter = afpaCenterRepository.findById(appUserDto.getIdAfpaCenter().getId()).orElse(null);
        if (afpaCenter == null) {
            throw new ResourceNotFoundException("Afpa center not found");
        }

        // Recherche et validation de l'id de la personne à contacter
        ContactPerson contactPerson = contactPersonRepository.findById(appUserDto.getIdContactPerson().getId()).orElse(null);
        if (contactPerson == null) {
            throw new ResourceNotFoundException("Contact person not found");
        }

        // Mappe le DTO en entité AppUser
        AppUser appUser = AppUserMapper.mapToAppUser(appUserDto);

        // Hacher le mot de passe avant de sauvegarder
        appUser.setPassword(passwordEncoder.encode(appUserDto.getPassword()));

        // Définir la date d'inscription à la date actuelle
        appUser.setRegistrationDate(LocalDate.now());

        try {

            // Sauvegarde l'entité AppUser dans le repository
            AppUser savedAppUser = appUserRepository.save(appUser);

            // Vérifier si le rôle de l'utilisateur enregistré a un 'ROLE_INTENDANT' ou 'ROLE_ADMIN'
            if (savedAppUser.getIdRole().getWordingRole().equals(RoleType.ROLE_INTENDANT)  || savedAppUser.getIdRole().getWordingRole().equals(RoleType.ROLE_ADMIN)) {
                // Créer une nouvelle instance de l'entité Intendant et associer l'utilisateur
                Intendant intendant = new Intendant();
                //associer l'utilisateur
                intendant.setIdUser(savedAppUser);
                //enregistrement dans l'entité
                intendantRepository.save(intendant);
            }

            // Mappe l'entité AppUser sauvegardée en DTO et la retourne
            return AppUserMapper.mapToAppUserDTO(savedAppUser);

        } catch (DataIntegrityViolationException e) {
            // Capturer une exception de violation d'intégrité des données
            throw new CreationException("Error occurred while creating an user");
        }

    }


    // Méthode pour obtenir tous les utilisateurs
    @Override
    public List<AppUserDTO> getAll() {

        // Récupère toutes les entités AppUser du repository
        List<AppUser> appUsers = appUserRepository.findAll();

        // Vérifie si la liste est vide
        if (appUsers.isEmpty()) {
            // Lance une NoContentException si aucune entité n'est trouvée
            throw new NoContentException(NO_USERS_FOUND);
        }

        try {
            // Mappe les entités AppUser en DTO et retourne la liste
            return appUsers.stream()
                    .map(AppUserMapper::mapToAppUserDTO)
                    .toList();

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while getting all users");
        }
    }


    @Override
    public Page<AppUserDTO> getAll(int page, int size) {

        // Crée un objet Pageable avec le numéro de page, la taille de la page, et le tri par ID croissant.
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        // Récupère toutes les entités Room du repository
        Page<AppUser> usersPage = appUserRepository.findAll(pageable);

        // Vérifie si la liste est vide
        if (usersPage.isEmpty()) {
            // Lance une ResourceNotFoundException si aucune entité n'est trouvée
            throw new NoContentException(NO_USERS_FOUND);
        }

        try {
            // Mappe les entités AppUser en DTO et retourne la liste
            return usersPage.map(AppUserMapper::mapToAppUserDTO);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while getting page of user(s)");
        }
    }


    public List<AppUserDTO> getAllByAfpaCenter(Integer idAfpaCenter) {

        //recherche de l'id du centre afpa dans le repository
        AfpaCenter afpacenter = afpaCenterRepository.findById(idAfpaCenter)
                .orElseThrow(() -> new ResourceNotFoundException("Afpa Center ID not found: " + idAfpaCenter));

        // Cherche les entités appUser par id afpa center
        List<AppUser> usersList = appUserRepository.findByIdAfpaCenter(afpacenter);

        // Vérifie si la liste est vide
        if (usersList.isEmpty()) {
            // Lance une ResourceNotFoundException si aucune entité n'est trouvée
            throw new NoContentException(NO_USERS_FOUND);
        }

        try {
            // Mappe les entités users en DTO et retourne la liste
            return usersList.stream()
                    .map(AppUserMapper::mapToAppUserDTO)
                    .toList();

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while getting users by ID");
        }

    }


    @Override
    public Page<AppUserDTO> getAllByAfpaCenter(Integer idAfpaCenter, Integer page, Integer size) {

        // Recherche de l'id du centre Afpa dans le repository
        AfpaCenter afpaCenter = afpaCenterRepository.findById(idAfpaCenter)
                .orElseThrow(() -> new ResourceNotFoundException("Afpa Center ID not found: " + idAfpaCenter));

        // Crée un objet Pageable avec le numéro de page, la taille de la page, et le tri par ID croissant
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        // Cherche les entités appUser par id Afpa center avec pagination
        Page<AppUser> usersPage = appUserRepository.findByIdAfpaCenter(afpaCenter, pageable);

        // Vérifie si la liste est vide
        if (usersPage.isEmpty()) {
            // Lance une NoContentException si aucune entité n'est trouvée
            throw new NoContentException(NO_USERS_FOUND);
        }

        try {
            // Mappe les entités users en DTO et retourne la page
            return usersPage.map(AppUserMapper::mapToAppUserDTO);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while getting user(s) by pagination");
        }
    }


    // Méthode pour obtenir un utilisateur par son ID
    @Override
    public Optional<AppUserDTO> getById(Integer id) {

        // Cherche l'utilisateur dans le repository par son ID
        AppUser findAppUser = appUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        try {
            // Convertit l'entité AppUser en AppUserDTO par le AppUserMapper
            AppUserDTO appUserDTO = AppUserMapper.mapToAppUserDTO(findAppUser);

            // Retourne le AppUserDTO encapsulé dans un Optional
            return Optional.of(appUserDTO);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred to recover user with ID: " + id);
        }
    }


    @Override
    public Optional<AppUserDTO> getByBeneficiaryNumber(String beneficiaryNumber) {

        // Cherche l'utilisateur dans le repository par son numéro de bénéficiaire
        AppUser findAppUser = appUserRepository.findByBeneficiaryNumber(beneficiaryNumber)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with beneficiary number: " + beneficiaryNumber));

        try {
            // Convertit l'entité AppUser en AppUserDTO par le AppUserMapper
            AppUserDTO appUserDTO = AppUserMapper.mapToAppUserDTO(findAppUser);

            // Retourne le AppUserDTO encapsulé dans un Optional
            return Optional.of(appUserDTO);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred to recover user with beneficiary number: " + beneficiaryNumber);
        }
    }


    @Override
    public Optional<AppUserDTO> getByEmail(String email) {

        // Cherche l'utilisateur dans le repository par son email
        AppUser findAppUser = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        try {
            // Convertit l'entité AppUser en AppUserDTO par le AppUserMapper
            AppUserDTO appUserDTO = AppUserMapper.mapToAppUserDTO(findAppUser);

            // Retourne le AppUserDTO encapsulé dans un Optional
            return Optional.of(appUserDTO);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred to recover user with email: " + email);
        }
    }


    // Méthode pour mettre à jour un utilisateur (réservé à l'admin !)
    @Override
    public Optional<AppUserDTO> update(Integer id, AppUserDTO appUserDto) {

        // Cherche l'entité AppUser à mettre à jour par son identifiant
        AppUser appUserToUpdate = appUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AppUser ID not found: " + id));


        // Met à jour les champs de l'entité AppUser avec les valeurs du DTO
        // si le champ n'est pas null et non vide alors mettre à jour les données
        if (appUserDto.getEmail() != null && !appUserDto.getEmail().isBlank()) {
            appUserToUpdate.setEmail(appUserDto.getEmail());
        }

        // Hacher et modifier le mot de passe seulement s'il est mis à jour
        if (appUserDto.getPassword() != null && !appUserDto.getPassword().isBlank()) {
            appUserToUpdate.setPassword(passwordEncoder.encode(appUserDto.getPassword()));
        }

        if (appUserDto.getPhoneNumber() != null && !appUserDto.getPhoneNumber().isBlank()) {
            appUserToUpdate.setPhoneNumber(appUserDto.getPhoneNumber());
        }

        if (appUserDto.getIsBlacklisted() != null) {
            appUserToUpdate.setIsBlacklisted(appUserDto.getIsBlacklisted());
        }

        if (appUserDto.getAddressUser() != null && !appUserDto.getAddressUser().isBlank()) {
            appUserToUpdate.setAddressUser(appUserDto.getAddressUser());
        }

        //attribut pouvant être null
        //si le champ est null ou n'est pas vide alors mise à jour des données
        if (appUserDto.getComplementUserAddress() == null || !appUserDto.getComplementUserAddress().isBlank()) {
            appUserToUpdate.setComplementUserAddress(appUserDto.getComplementUserAddress());
        }

        // Mise à jour des Many-to-One
        // recherche l'id du role dans le repository
        Role role = roleRepository.findById(appUserDto.getIdRole().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Role ID not found"));

        // Mise à jour du role associé
        if (appUserDto.getIdRole() != null) {

            // Mettre à jour la civilité associée
            appUserToUpdate.setIdRole(role);

        } else {
            throw new IllegalArgumentException("id role cannot be null");
        }

        // Recherche de l'id de la civilité dans le repository
        Civility civility = civilityRepository.findById(appUserDto.getIdCivility().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Civility ID not found"));

        // Mise à jour de la civilité associée
        if (appUserDto.getIdCivility() != null) {

            // Mettre à jour la civilité associée
            appUserToUpdate.setIdCivility(civility);

        } else {
            throw new IllegalArgumentException("id civility cannot be null");
        }

        // Recherche de l'id de la ville dans le repository
        City city = cityRepository.findById(appUserDto.getIdCity().getId())
                .orElseThrow(() -> new ResourceNotFoundException("City ID not found"));

        // Mise à jour de la ville associée
        if (appUserDto.getIdCity() != null) {

            // Mettre à jour la ville associée
            appUserToUpdate.setIdCity(city);

        } else {
            throw new IllegalArgumentException("id city cannot be null");
        }

        // Recherche de l'id de la personne à contacter dans le repository
        ContactPerson contactPerson = contactPersonRepository.findById(appUserDto.getIdContactPerson().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Contact person ID not found"));

        // Mise à jour de la personne à contacter associée
        if (appUserDto.getIdCity() != null) {

            // Mettre à jour la person à contacter associée
            appUserToUpdate.setIdContactPerson(contactPerson);

        } else {
            throw new IllegalArgumentException("id contact person cannot be null");
        }

        try {
            // Sauvegarde l'entité mise à jour dans le repository
            AppUser updatedAppUser = appUserRepository.save(appUserToUpdate);

            // Vérifier si le rôle de l'utilisateur enregistré a un 'ROLE_INTENDANT' ou 'ROLE_ADMIN'
            if (updatedAppUser.getIdRole().getWordingRole() == RoleType.ROLE_INTENDANT || updatedAppUser.getIdRole().getWordingRole() == RoleType.ROLE_ADMIN) {
                // Créer une nouvelle instance de l'entité Intendant et associer l'utilisateur
                Intendant intendant = new Intendant();
                //associer l'utilisateur
                intendant.setIdUser(updatedAppUser);
                //enregistrement dans l'entité
                intendantRepository.save(intendant);
            }

            // Convertit l'entité mise à jour en DTO et la retourne
            return Optional.of(AppUserMapper.mapToAppUserDTO(updatedAppUser));

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while updating AppUser");
        }
    }


    // Méthode pour mettre à jour un utilisateur (réservé à l'user !)
    public Optional<UserDTO> updateUser(Integer id, UserDTO userDto) {

        // Cherche l'entité AppUser à mettre à jour par son identifiant
        AppUser userToUpdate = appUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AppUser ID not found: " + id));

        // Met à jour les champs de l'entité AppUser avec les valeurs du DTO s'ils ne sont pas null et pas vide
        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
            userToUpdate.setEmail(userDto.getEmail());
        }

        if (userDto.getPhoneNumber() != null && !userDto.getPhoneNumber().isBlank()) {
            userToUpdate.setPhoneNumber(userDto.getPhoneNumber());
        }

        if (userDto.getAddressUser() != null && !userDto.getAddressUser().isBlank()) {
            userToUpdate.setAddressUser(userDto.getAddressUser());
        }

        //attribut pouvant être null
        if (userDto.getComplementUserAddress() == null || !userDto.getComplementUserAddress().isBlank()) {
            userToUpdate.setComplementUserAddress(userDto.getComplementUserAddress());
        }


        // Mise à jour des Many-to-One
        // Recherche de l'id de la civilité dans le repository
        Civility civility = civilityRepository.findById(userDto.getIdCivility().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Civility ID not found"));


        // Mise à jour de la civilité associée
        if (userDto.getIdCivility() != null) {

            // Mettre à jour la civilité associée
            userToUpdate.setIdCivility(civility);

        } else {
            throw new IllegalArgumentException("id civility cannot be null");
        }

        // Recherche de l'id de la ville dans le repository
        City city = cityRepository.findById(userDto.getIdCity().getId())
                .orElseThrow(() -> new ResourceNotFoundException("City ID not found"));

        // Mise à jour de la ville associée
        if (userDto.getIdCity() != null) {

            // Mettre à jour la ville associée
            userToUpdate.setIdCity(city);

        } else {
            throw new IllegalArgumentException("id city cannot be null");
        }

        // Recherche de l'id de la personne à contacter dans le repository
        ContactPerson contactPerson = contactPersonRepository.findById(userDto.getIdContactPerson().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Contact person ID not found"));

        // Mise à jour de la personne à contacter associée
        if (userDto.getIdCity() != null) {

            // Mettre à jour la person à contacter associée
            userToUpdate.setIdContactPerson(contactPerson);

        } else {
            throw new IllegalArgumentException("id contact person cannot be null");
        }

        try {
            // Sauvegarde l'entité mise à jour dans le repository
            AppUser updatedAppUser = appUserRepository.save(userToUpdate);

            // Convertit l'entité mise à jour en DTO et la retourne
            return Optional.of(UserMapper.mapToUserDTO(updatedAppUser));

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while updating an user");
        }
    }


    public void updatePassword(Integer id, PasswordDTO passwordDto) {

        // Trouver l'utilisateur par son ID
        AppUser userToUpdate = appUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Vérifier que le nouveau mot de passe n'est pas null ou vide
        if (passwordDto.getNewPassword() == null || passwordDto.getNewPassword().isBlank() || passwordDto.getCurrentPassword() == null || passwordDto.getCurrentPassword().isBlank()) {
            throw new IllegalArgumentException(" field cannot be null or blank");
        }

        // Vérifier que le mot de passe actuel est correct
        if (!passwordEncoder.matches(passwordDto.getCurrentPassword(), userToUpdate.getPassword())) {
            throw new IllegalArgumentException("password is incorrect");
        }

        // Mettre à jour le mot de passe avec le nouveau mot de passe haché
        userToUpdate.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));

        try {
            // Sauvegarder l'utilisateur mis à jour dans le repository
            appUserRepository.save(userToUpdate);

        } catch (Exception e) {
            // Capturer toute exception inattendue et lancer une exception avec un message d'erreur
            throw new InternalServerException("An error occurred while updating the password" + e.getMessage());
        }
    }


    // Méthode pour supprimer un utilisateur par son ID
    @Override
    public void deleteById(Integer id) {

        // Tente de trouver l'entité AppUser par son identifiant
        AppUser appUser = appUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found with ID: " + id));

        try {
            // Supprime l'entité AppUser trouvée
            appUserRepository.delete(appUser);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("An unexpected error occurred while deleting user with ID: " + id);
        }
    }


}
