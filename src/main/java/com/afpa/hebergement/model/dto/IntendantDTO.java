package com.afpa.hebergement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IntendantDTO {

    private Integer id;
    private LocalDate startDateIntendant;
    private LocalDate endDateIntendant;

    //relation Many to One
    private AppUserDTO idUser;

}
