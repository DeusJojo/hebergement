package com.afpa.hebergement.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class LeaseContractFormDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dayDate;
    private String name;
    private String firstname;
    private LocalDate birthdate;
    private String wordingCivility;
    private String cityName;
    private String formationName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate startDateLease;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate endDateLease;

    private String centerName;
    private String centerManager;
    private String addressCenter;
    private String centerCityName;
    private String centerPostCode;
    private String roomNumber;
    private Integer numberOfPerson;
    private BigDecimal totalRent;
    private String frequency;
    private Double totalDeposit;
    private List<String> listService;

}
