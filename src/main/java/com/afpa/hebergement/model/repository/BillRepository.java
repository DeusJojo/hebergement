package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.Absence;
import com.afpa.hebergement.model.entity.Bill;
import com.afpa.hebergement.model.entity.LeaseContract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill,Integer> {
    //recherche par billNumber et id_leaseContract
    Optional<Bill> findByBillNumberAndIdLease(String billNumber, LeaseContract idleaseContract);

}
