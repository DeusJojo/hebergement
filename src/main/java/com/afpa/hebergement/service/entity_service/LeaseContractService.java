package com.afpa.hebergement.service.entity_service;

import com.afpa.hebergement.model.dto.LeaseContractDTO;
import com.afpa.hebergement.model.dto.LeaseContractFormDTO;
import com.afpa.hebergement.service.GenericService;

import java.util.List;


public interface LeaseContractService extends GenericService<LeaseContractDTO, Integer> {

    //méthode pour créer un LeaseContractDTO via le DTO LeaseContractFormDTO
    LeaseContractDTO create(LeaseContractFormDTO leaseContractFormDTO);

    //méthode pour récupérer la liste des contrats de bail par centre afpa
    List<LeaseContractDTO> getAllByAfpaCenter(Integer idAfpaCenter);

}
