package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.AfpaCenter;
import com.afpa.hebergement.model.entity.LeaseContract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaseContractRepository  extends JpaRepository<LeaseContract, Integer> {

    //rechercher un contrat de bail par centre afpa
    List<LeaseContract> findByIdUser_IdAfpaCenter(AfpaCenter idAfpaCenter);

}
