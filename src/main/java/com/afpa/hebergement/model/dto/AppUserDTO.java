package com.afpa.hebergement.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDTO {


    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;
    private String beneficiaryNumber;
    private String name;
    private String firstname;
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private LocalDate birthdate;
    private String birthplace;
    private String phoneNumber;
    private Boolean isBlacklisted;
    private LocalDate registrationDate; //creation admin
    private String addressUser;
    private String complementUserAddress;

    //Many to One
    private RoleDTO idRole;
    private CivilityDTO idCivility;
    private CityDTO idCity;
    private AfpaCenterDTO idAfpaCenter;
    private ContactPersonDTO idContactPerson;

}
