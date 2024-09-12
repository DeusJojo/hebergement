package com.afpa.hebergement.service.entity_service;

import com.afpa.hebergement.model.dto.BillDTO;
import com.afpa.hebergement.model.dto.BillFormDTO;
import com.afpa.hebergement.service.GenericService;

public interface BillService extends GenericService<BillDTO,Integer> {
    //méthode pour créer un BillDTO via le BillFormDTO
    BillDTO create(BillFormDTO billFormDTO);

}
