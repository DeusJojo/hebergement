package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.CreationException;
import com.afpa.hebergement.exception.InternalServerException;
import com.afpa.hebergement.exception.NoContentException;
import com.afpa.hebergement.exception.ResourceNotFoundException;
import com.afpa.hebergement.model.dto.LeaseContractDTO;
import com.afpa.hebergement.model.dto.LeaseContractFormDTO;
import com.afpa.hebergement.model.entity.*;
import com.afpa.hebergement.model.mapper.LeaseContractMapper;
import com.afpa.hebergement.model.repository.*;
import com.afpa.hebergement.service.entity_service.LeaseContractService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;


@Service
@AllArgsConstructor
public class LeaseContractServiceImpl implements LeaseContractService {


    public static final String DD_MM_YYYY = "dd/MM/yyyy";
    private final LeaseContractRepository leaseContractRepository;
    private final RoomRepository roomRepository;
    private final AppUserRepository appUserRepository;
    private final AfpaCenterRepository afpaCenterRepository;
    private final CivilityRepository civilityRepository;
    private final AppServiceRepository appServiceRepository;
    private final RentRepository rentRepository;
    private final ParticipateRepository participateRepository;


    @Override
    public LeaseContractDTO create(LeaseContractFormDTO leaseContractFormDto) {


        // Vérifier que la date de début est antérieure à la date de fin
        if (leaseContractFormDto.getStartDateLease().isAfter(leaseContractFormDto.getEndDateLease())) {
            throw new IllegalArgumentException("The start date of the contract cannot be later than the end date.");
        }

        // Vérifier que la date de début et la date de fin ne sont pas dans le passé
        if (leaseContractFormDto.getStartDateLease().isBefore(LocalDate.now()) || leaseContractFormDto.getEndDateLease().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Contract dates cannot be in the past.");
        }

        // Vérifier la durée minimale du contrat (par exemple, au moins 1 jour)
        if (leaseContractFormDto.getStartDateLease().isEqual(leaseContractFormDto.getEndDateLease())) {
            throw new IllegalArgumentException("The lease contract must be for a duration of at least one day.");
        }

        // Rechercher un utilisateur par son nom, prénom, date de naissance
        AppUser user = appUserRepository.findByNameAndFirstnameAndBirthdate(
                        leaseContractFormDto.getName(),
                        leaseContractFormDto.getFirstname(),
                        leaseContractFormDto.getBirthdate())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        //rechercher une civilité
        Civility civility = civilityRepository.findByWordingCivility(leaseContractFormDto.getWordingCivility()).orElse(null);
        if ( civility == null) {
            throw new ResourceNotFoundException("Civility not found with wording: " + leaseContractFormDto.getWordingCivility());
        }

        // Rechercher un centre Afpa par son nom de centre
        AfpaCenter afpaCenter = afpaCenterRepository.findByCenterName(leaseContractFormDto.getCenterName())
                .orElseThrow(() -> new ResourceNotFoundException("Afpa center not found with name " + leaseContractFormDto.getCenterName()));

        // Rechercher une chambre par son numéro et par ID du centre Afpa
        Room room = roomRepository.findByRoomNumberAndIdFloor_IdAfpaCenter_Id(
                        leaseContractFormDto.getRoomNumber(),
                        afpaCenter.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with room number: " + leaseContractFormDto.getRoomNumber() + " and id Afpa center: " + afpaCenter.getId()));

        // Rechercher la dernière session de formation par l'ID utilisateur
        Optional<Participate> lastSessionFormationOptional = participateRepository.findFirstByIdUser_IdOrderByIdDesc(user.getId());

        // Rechercher un loyer par fréquence et par ID du centre Afpa
        Rent rent = rentRepository.findByFrequencyAndIdAfpaCenter_Id(
                        leaseContractFormDto.getFrequency(),
                        afpaCenter.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Rent not found for : " + afpaCenter.getCenterName()));

        // rechercher les services dans la liste, vérifier s'ils sont bien existants dans la base de données
        List<String> listService = leaseContractFormDto.getListService();
        for (String serviceName : listService) {
            AppService service = appServiceRepository.findByWordingService(serviceName).orElse(null);
            if (service == null) {
                throw new ResourceNotFoundException("Service not found with name: " + serviceName);
            }
        }

        // Compléter les données du contrat de bail (auto-complétion) en fonction de l'utilisateur trouvé
        leaseContractFormDto.setCityName(user.getIdCity().getCityName());

        // Définir le champ formationName en fonction de la session de formation trouvée
        if (lastSessionFormationOptional.isEmpty()) {
            leaseContractFormDto.setFormationName("");
        } else {
            Participate lastSessionFormation = lastSessionFormationOptional.get();
            leaseContractFormDto.setFormationName(lastSessionFormation.getIdSession().getIdFormation().getFormationName());
        }

        // Compléter les informations du centre Afpa dans le DTO
        leaseContractFormDto.setWordingCivility(user.getIdCivility().getWordingCivility());
        leaseContractFormDto.setCenterName(user.getIdAfpaCenter().getCenterName());
        leaseContractFormDto.setCenterManager(user.getIdAfpaCenter().getCenterManager());
        leaseContractFormDto.setAddressCenter(user.getIdAfpaCenter().getAddressCenter());
        leaseContractFormDto.setCenterPostCode(user.getIdAfpaCenter().getIdCity().getPostcode());
        leaseContractFormDto.setCenterCityName(user.getIdAfpaCenter().getIdCity().getCityName());
        leaseContractFormDto.setTotalRent(rent.getAmount());

        // Mapper LeaseContractFormDTO en LeaseContract
        LeaseContract leaseContract = LeaseContractMapper.mapLeaseContractFormDtoToLeaseContract(leaseContractFormDto, user, room, rent);

        try {
            // Sauvegarder le contrat de location dans le repository
            LeaseContract savedLeaseContract = leaseContractRepository.save(leaseContract);

            // Retourner l'entité lease contract créée en DTO
            return LeaseContractMapper.mapToLeaseContractDTO(savedLeaseContract);

        } catch (Exception e) {
            // Gestion des exceptions lors de la génération du PDF
            throw new CreationException("Error creating a lease contract: " + e.getMessage());
        }
    }


    @Override
    public List<LeaseContractDTO> getAllByAfpaCenter(Integer idAfpaCenter) {

        // Recherche de l'id du centre Afpa dans le repository
        AfpaCenter afpaCenter = afpaCenterRepository.findById(idAfpaCenter)
                .orElseThrow(() -> new ResourceNotFoundException("Afpa Center ID not found: " + idAfpaCenter));

        // Cherche les entités LeaseContract par id Afpa center
        List<LeaseContract> leaseContractList = leaseContractRepository.findByIdUser_IdAfpaCenter(afpaCenter);

        // Vérifie si la liste est vide
        if (leaseContractList.isEmpty()) {
            // Lance une NoContentException si aucun contrat n'est trouvé
            throw new NoContentException("No lease contracts found for Afpa Center ID: " + idAfpaCenter);
        }

        try {
            // Mappe les entités LeaseContract en DTO et retourne la liste
            return leaseContractList.stream()
                    .map(LeaseContractMapper::mapToLeaseContractDTO)
                    .toList();

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred while getting lease contracts by Afpa Center ID: " + idAfpaCenter);
        }
    }


    @Override
    public Optional<LeaseContractDTO> getById(Integer id) {

        // Cherche une entité Lease Contract par son identifiant
        LeaseContract findLeaseContract = leaseContractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lease contract not found with id: " + id));

        try {
            // Convertit l'entité Lease Contract en LeaseContractDTO par le LeaseContractMapper
            LeaseContractDTO leaseContractDTO = LeaseContractMapper.mapToLeaseContractDTO(findLeaseContract);

            // Retourne le leaseContractDTO encapsulé dans un Optional
            return Optional.of(leaseContractDTO);

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred to recover lease contract  with ID: " + id);
        }
    }


    @Override
    public void deleteById(Integer id) {

        // Tente de trouver l'entité Lease Contract par son identifiant
        LeaseContract leaseContract= leaseContractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lease contract not found with id: " + id));

        try {
            // Supprime l'entité Lease Contract trouvée
            leaseContractRepository.deleteById(leaseContract.getId());

        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("An unexpected error occurred while deleting the lease contract with id: " + id);
        }
    }


}
