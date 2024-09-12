package com.afpa.hebergement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {

    private Integer id;
    private String roomNumber;
    private Integer roomKeyNumber;
    private Boolean isUsable;
    private Integer badgeNumber;
    private Integer fridgeKeyNumber;
    private Boolean isReserved;

    //Many to One
    private FloorDTO idFloor;

}
