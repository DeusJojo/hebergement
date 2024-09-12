package com.afpa.hebergement.model.payload.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class SignupRequest {

    @NotBlank
    @Size(min = 3, max = 150)
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 150)
    private String name;

    @NotBlank
    private String birthdate;

    @NotBlank
    private String phone;

    @NotBlank
    private String birthPlace;

    @NotBlank
    private String postCode;

    @NotBlank
    private String inseeCode;

    @NotNull
    private Integer civility;

    @NotNull
    private Integer idContactPerson;

    @NotNull
    private Integer idCenter;

    @NotBlank
    @Size(min = 3, max = 20)
    private String number;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private String role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotBlank
    @Size(max = 255)
    private String address;


}
