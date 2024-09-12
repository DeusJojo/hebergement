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
public class MessageDTO {

    private Integer id;
    private String content;
    private LocalDate messageDate;

    //relation Many to One
    private IntendantDTO idIntendant;
    private AppUserDTO idUser;

}


