package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.LeaseContractDTO;
import com.afpa.hebergement.model.dto.LeaseContractFormDTO;
import com.afpa.hebergement.model.entity.AppUser;
import com.afpa.hebergement.model.entity.LeaseContract;
import com.afpa.hebergement.model.entity.Rent;
import com.afpa.hebergement.model.entity.Room;


public class LeaseContractMapper {

    // Constructeur privé pour empêcher l'instanciation de cette classe
    private LeaseContractMapper() {
    }

    // Méthode pour mapper une entité LeaseContract vers un DTO LeaseContractDTO
    public static LeaseContractDTO mapToLeaseContractDTO(LeaseContract leaseContract) {

        // Créer un objet LeaseContractDTO
        LeaseContractDTO leaseContractDTO = new LeaseContractDTO();

        // Affecte les champs de l'entité LeaseContract au DTO LeaseContractDTO
        leaseContractDTO.setId(leaseContract.getId());
        leaseContractDTO.setStartDateLease(leaseContract.getStartDateLease());
        leaseContractDTO.setEndDateLease(leaseContract.getEndDateLease());
        leaseContractDTO.setIsPresent(leaseContract.getIsPresent());

        // Mappage Many to One
        leaseContractDTO.setIdUser(AppUserMapper.mapToAppUserDTO(leaseContract.getIdUser()));
        leaseContractDTO.setIdRoom(RoomMapper.mapToRoomDTO(leaseContract.getIdRoom()));


        // Retourne le DTO LeaseContractDTO
        return leaseContractDTO;
    }


    // Méthode pour mapper un DTO LeaseContractDTO vers une entité LeaseContract
    public static LeaseContract mapToLeaseContract(LeaseContractDTO leaseContractDto) {

        // Créer un objet LeaseContract
        LeaseContract leaseContract = new LeaseContract();

        // Affecte les champs du DTO LeaseContractDTO à l'entité LeaseContract
        leaseContract.setId(leaseContractDto.getId());
        leaseContract.setStartDateLease(leaseContractDto.getStartDateLease());
        leaseContract.setEndDateLease(leaseContractDto.getEndDateLease());
        leaseContract.setIsPresent(leaseContractDto.getIsPresent());

        // Mappage Many to One
        leaseContract.setIdUser(AppUserMapper.mapToAppUser(leaseContractDto.getIdUser()));
        leaseContract.setIdRoom(RoomMapper.mapToRoomEntity(leaseContractDto.getIdRoom()));


        // Retourne l'entité LeaseContract
        return leaseContract;
    }


    // Méthode pour mapper un DTO LeaseContractFormDTO vers une entité LeaseContract
    public static LeaseContract mapLeaseContractFormDtoToLeaseContract(LeaseContractFormDTO formDto, AppUser appUser, Room room, Rent rent) {

        //creation d'un nouveau contrat
        LeaseContract leaseContract = new LeaseContract();

        //mappage des dates du contrat
        leaseContract.setStartDateLease(formDto.getStartDateLease());
        leaseContract.setEndDateLease(formDto.getEndDateLease());

        //many to one
        leaseContract.setIdUser(appUser);
        leaseContract.setIdRoom(room);
        leaseContract.setIdRent(rent);

        return leaseContract;
    }

}
