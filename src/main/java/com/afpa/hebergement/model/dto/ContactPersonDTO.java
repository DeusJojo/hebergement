package com.afpa.hebergement.model.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ContactPersonDTO {

    private Integer id;
    private String name;
    private String firstname;

    // N'accepte que les caractères spéciaux et les chiffres
    @Pattern(regexp = "^\\+?[0-9 ()-]{7,25}$", message = "Invalid phone number format")
    private String phoneNumberContact;
}
