package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.AppUser;
import com.afpa.hebergement.model.entity.Deposit;
import com.afpa.hebergement.model.entity.Guarantee;
import com.afpa.hebergement.model.entity.GuaranteeType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface DepositRepository extends JpaRepository<Deposit, Integer> {

    Optional<Deposit> findByIdUser(AppUser user);
    Optional<Deposit> findByIdGuarantee(Guarantee guarantee);
    Optional<Deposit> findByIdUserAndIdGuarantee_idGuaranteeType(AppUser user, GuaranteeType guarantyType);
}
