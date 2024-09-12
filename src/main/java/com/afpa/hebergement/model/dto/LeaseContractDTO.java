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
public class LeaseContractDTO {

    private Integer id;
    private LocalDate startDateLease;
    private LocalDate endDateLease;
    private Boolean isPresent;

    // Many to One
    private AppUserDTO idUser;
    private RoomDTO idRoom;

}

