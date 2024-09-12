package com.afpa.hebergement.model.dto;

import com.afpa.hebergement.model.entity.LeaseContract;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class BillFormDTO {
    private Integer idLease;
    private String billNumber;
    private String month;
    private String year;
    private String centerName;
    private String addressCenter;
    private String centerPostCode;
    private String centerCityName;
    private String wordingCivility;
    private String name;
    private String firstname;
    private LocalDate billDate;
    private LocalDate paymentDateBill;
    private BigDecimal totalAmount;
    private List<PaymentTypeDTO> paymentTypes;


}
