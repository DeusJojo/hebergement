package com.afpa.hebergement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String email;
    private String phoneNumber;
    private String addressUser;
    private String complementUserAddress;

    //Many to One
    private CivilityDTO idCivility;
    private CityDTO idCity;
    private ContactPersonDTO idContactPerson;

}
