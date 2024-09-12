package com.afpa.hebergement.model.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AfpaCenterDTO {

    private Integer id;
    private String centerName;
    private String addressCenter;
    private String complementAddrCenter;

    // N'accepte que les Strings de 9 caractères
    @Pattern(regexp = "^\\d{9}$", message = "Numéro de SIREN invalide.")
    private String siren;

    // Vérifie le numéro de TVA
    @Pattern(regexp = "^FR\\d{2}\\d{9}$", message = "Invalid French VAT number format")
    private String tvaNumber;

    private String centerPhoneNum;
    private String faxCenter;
    private String centerManager;
    private String codeCenter;
    private CityDTO idCity;

}
