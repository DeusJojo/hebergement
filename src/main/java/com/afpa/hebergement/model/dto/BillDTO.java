package com.afpa.hebergement.model.dto;

import com.afpa.hebergement.model.entity.LeaseContract;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class BillDTO {
    private  Integer id;
    private String billNumber;
    private BigDecimal totalAmount;
    private LocalDate billDate;
    private Boolean isPayed = false;
    private LocalDate paymentDateBill;

    //relation Many to One
    private LeaseContractDTO idLease;

    //relation Many to Many
    private Set<PaymentTypeDTO> paymentTypes;
}
