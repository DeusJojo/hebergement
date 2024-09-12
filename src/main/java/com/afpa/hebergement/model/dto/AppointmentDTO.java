package com.afpa.hebergement.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {

    private Integer id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    // dans swagger le format appointment : "dateAppointment" : "30/08/2024 07:58"
    private LocalDateTime dateAppointment;

    private String reason;

    //relation Many to One
    private IntendantDTO idIntendant;
    private AppUserDTO idUser;

}
